package com.lufin.server.stock.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.stock.domain.StockNews;
import com.lufin.server.stock.domain.StockPortfolio;
import com.lufin.server.stock.domain.StockPriceHistory;
import com.lufin.server.stock.domain.StockProduct;
import com.lufin.server.stock.dto.StockPriceHistoryRequestDto;
import com.lufin.server.stock.dto.StockPriceHistoryResponseDto;
import com.lufin.server.stock.dto.StockResponseDto;
import com.lufin.server.stock.repository.StockNewsRepository;
import com.lufin.server.stock.repository.StockPortfolioRepository;
import com.lufin.server.stock.repository.StockPriceHistoryRepository;
import com.lufin.server.stock.repository.StockProductRepository;
import com.lufin.server.stock.util.JsonToDtoUtil;
import com.lufin.server.stock.util.StockAiPrompt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockPriceHistoryServiceImpl implements StockPriceHistoryService {
	private final StockProductRepository stockProductRepository;
	private final StockPriceHistoryRepository stockPriceHistoryRepository;
	private final StockPortfolioRepository stockPortfolioRepository;
	private final StockNewsRepository stockNewsRepository;

	private final StockAiService stockAiService;

	private static final int maxRetries = 30;
	private static int retryCount = 0;
	private static boolean isNewsCreated = false;
	private static long waitTime = 1000L;

	/**
	 * 주식 가격 변동 기록 조회
	 * @param stockProductId
	 * @param counts
	 * @return
	 */
	@Override
	public List<StockPriceHistoryResponseDto.PriceHistoryResponseDto> getStockPriceHistory(Integer stockProductId,
		Integer counts) {
		log.info("주식 가격 변동 조회 요청: stockProductId = {}, days = {}", stockProductId, counts);

		if (stockProductId == null || counts == null) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}

		try {
			List<StockPriceHistory> priceHistoryList = stockPriceHistoryRepository.findLatestPriceByStockProductIdAndCount(
				stockProductId,
				counts
			);

			if (priceHistoryList == null || priceHistoryList.isEmpty()) {
				log.warn("주식 가격 변동 조회 실패");
				throw new BusinessException(INVESTMENT_PRICE_NOT_FOUND);
			}

			return priceHistoryList.stream()
				.map(
					StockPriceHistoryResponseDto.PriceHistoryResponseDto::stockPriceHistoryEntityToPriceHistoryResponseDto)
				.toList();
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage());
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Scheduled(cron = "0 5 9 * * Mon-Fri")
	@Transactional
	@Override
	public synchronized void updateMorningStockPrice() { // 스케줄러 중첩 실행 방지를 위해 synchronized 사용
		log.info("09:05 주식 가격 업데이트 스케줄 작업 시작: hour = {}", LocalDateTime.now().getHour());
		try {
			List<StockResponseDto.StockInfoDto> stocks = stockProductRepository.getAllStocks();
			for (StockResponseDto.StockInfoDto stock : stocks) {
				updateStockPrice(stock.stockProductId(), LocalDateTime.now().getHour());
				log.info("주식 ID: {}에 대한 오전 가격 변동 성공적으로 반영됨", stock.stockProductId());
			}

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Scheduled(cron = "0 5 13 * * Mon-Fri")
	@Transactional
	@Override
	public synchronized void updateAfternoonStockPrice() { // 스케줄러 중첩 실행 방지를 위해 synchronized 사용
		log.info("13:05 주식 가격 업데이트 스케줄 작업 시작: hour = {}", LocalDateTime.now().getHour());
		try {
			List<StockResponseDto.StockInfoDto> stocks = stockProductRepository.getAllStocks();
			for (StockResponseDto.StockInfoDto stock : stocks) {
				updateStockPrice(stock.stockProductId(), LocalDateTime.now().getHour());
				log.info("주식 ID: {}에 대한 오후 가격 변동 성공적으로 반영됨", stock.stockProductId());
			}
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage());
			throw new BusinessException(SERVER_ERROR);
		}
	}

	/**
	 * 주식 가격 변동 생성 메서드
	 * @param stockProductId
	 * @param hour
	 */
	@Transactional
	@Override
	public void updateStockPrice(
		Integer stockProductId,
		Integer hour
	) {
		log.info("특정 주식 가격 업데이트 요청: stockProductId = {}, LocalDateTime = {}", stockProductId,
			LocalDateTime.now());

		if (stockProductId == null) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}

		/* 동시성 문제 방지를 위해 아토믹 업데이트 및 중복 체크 적용 */
		LocalDateTime now = LocalDateTime.now();
		// 오늘 날짜의 지정된 시간으로 설정
		LocalDateTime startTime = LocalDateTime.of(
			now.getYear(),
			now.getMonth(),
			now.getDayOfMonth(),
			hour, // 9시 또는 13시
			0,    // 0분
			0     // 0초
		);
		LocalDateTime endTime = startTime.plusHours(1); // 1시간 차이 이내에 있는 지 확인

		while (!isNewsCreated && retryCount < maxRetries) {
			try {
				Integer existsInTimeRangeNews = stockNewsRepository.existsByStockProductIdAndCreatedAtBetween(
					stockProductId, startTime, endTime
				);

				if (existsInTimeRangeNews == 1) {
					isNewsCreated = true;
					log.info("{}시 뉴스 생성 확인. 주식 가격 업데이트를 시작합니다.", hour);
					break;
				} else {
					retryCount++;
					log.info("{}시 뉴스가 아직 생성되지 않았습니다. 재시도 중... ({}/{})", hour, retryCount, maxRetries);
					waitTime = Math.min(waitTime * 2, 30000);
					Thread.sleep(waitTime); // 최대 30초 대기 후 재시도
				}
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				log.error("주식 가격 변동 생성 재시도 중 인터럽트 발생", ie.getMessage());
				break;

			} catch (Exception e) {
				log.error("주식 가격 변동 생성 중 오류 발생: {}", e.getMessage());
				retryCount++;
				try {
					waitTime = Math.min(waitTime * 2, 30000);
					Thread.sleep(waitTime); // 최대 30초 대기 후 재시도
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					break;
				}
			}

		}

		if (!isNewsCreated) {
			log.error("최대 재시도 횟수({})에 도달했습니다. {}시 뉴스가 생성되지 않아 주식 가격 변동 생성을 진행할 수 없습니다.", maxRetries, hour);
			throw new BusinessException(SERVER_ERROR);
		}

		Integer existsInTimeRangePrice = stockPriceHistoryRepository.existsByStockProductIdAndCreatedAtBetween(
			stockProductId, startTime, endTime
		);

		if (existsInTimeRangePrice == 1) {
			log.warn("해당 시간대에 이미 생성된 가격 변동이 있습니다: stockProductId = {}, hour = {}",
				stockProductId, hour);
			throw new BusinessException(ErrorCode.DUPLICATE_UPDATE);
		}

		try {
			// 1. stockProductId로 stockProduct 객체 조회
			StockProduct stockProduct = stockProductRepository.findById(stockProductId)
				.orElseThrow(() -> new BusinessException(ErrorCode.INVESTMENT_PRODUCT_NOT_FOUND));

			log.info("stockProduct 조회: {}", stockProduct.toString());

			// 2. product에 대한  <- portfolio에서 productId에 대해 조회한 후 모든 total끼리 합쳐서 계산
			List<StockPortfolio> portfolios = stockPortfolioRepository.findByStockProductId(stockProductId);

			int totalQuantity = 0;
			int totalPurchaseAmount = 0;
			int totalSellAmount = 0;

			for (StockPortfolio portfolio : portfolios) {
				totalQuantity += portfolio.getQuantity();
				totalPurchaseAmount += portfolio.getTotalPurchaseAmount();
				totalSellAmount += portfolio.getTotalSellAmount();
			}

			log.info("주식 상품 정보 계산: totalQuantity = {}, totalPurchaseAmount = {}, totalSellAmount = {}", totalQuantity,
				totalPurchaseAmount, totalSellAmount);

			// 3. 가장 최근 뉴스 조회
			Optional<StockNews> recentNews = stockNewsRepository.findLatestNewsByStockProductId(stockProductId);

			if (!recentNews.isPresent()) {
				log.warn("뉴스 조회에 실패했습니다. stockProductId = {}", stockProductId);
				throw new BusinessException(SERVER_ERROR);
			}

			StockNews news = recentNews.get();

			String prompt = StockAiPrompt.pricePrompt(
				stockProduct,
				news,
				totalQuantity,
				totalPurchaseAmount,
				totalSellAmount
			);
			log.info("AI 가격 변동 정보 생성 요청: prompt = {}", prompt.length());

			String priceContent;

			try {
				priceContent = stockAiService.generateResponse(prompt);
				log.info("AI 가격 변동 정보 생성 요청: newsContent = {}", priceContent);
			} catch (Exception e) {
				throw new BusinessException(GENERATE_FAILED);
			}

			if (priceContent == null || priceContent.isEmpty()) {
				throw new BusinessException(GENERATE_FAILED);
			}

			StockPriceHistoryRequestDto.PriceHistoryAiInfoDto request = JsonToDtoUtil.convertPrice(priceContent);

			StockPriceHistory priceHistory = StockPriceHistory.create(
				request.currentPrice(),
				stockProduct
			);

			StockPriceHistory savedPriceHistory = stockPriceHistoryRepository.save(priceHistory);

			log.info("가격 변동 내역 저장 완료: {}", savedPriceHistory.getId());

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}

	}
}
