package com.lufin.server.stock.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.stock.domain.StockNews;
import com.lufin.server.stock.domain.StockPortfolio;
import com.lufin.server.stock.domain.StockProduct;
import com.lufin.server.stock.dto.StockNewsRequestDto;
import com.lufin.server.stock.dto.StockNewsResponseDto;
import com.lufin.server.stock.repository.StockNewsRepository;
import com.lufin.server.stock.repository.StockPortfolioRepository;
import com.lufin.server.stock.repository.StockProductRepository;
import com.lufin.server.stock.util.StockAiPrompt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockNewsServiceImpl implements StockNewsService {
	private final StockNewsRepository stockNewsRepository;
	private final StockProductRepository stockProductRepository;
	private final StockPortfolioRepository stockPortfolioRepository;

	private final StockAiService stockAiService;

	/**
	 * 특정 주식 공시 정보 목록 조회
	 */
	@Override
	public List<StockNewsResponseDto.NewsInfoDto> getAllNews(Integer stockProductId) {
		log.info("특정 주식 공시 정보 목록 조회: stockProductId = {}", stockProductId);

		if (stockProductId == null) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}

		try {
			List<StockNewsResponseDto.NewsInfoDto> result = stockNewsRepository.getAllNews(stockProductId);

			return result;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}

	}

	/**
	 * 특정 주식 공시 정보 상세 조회
	 */
	@Override
	public StockNewsResponseDto.NewsInfoDto getNewsByNewsId(Integer stockProductId, Integer newsId) {
		log.info("특정 주식 공시 정보 상세 조회: stockProductId = {}, newsId = {}", stockProductId, newsId);

		if (stockProductId == null || newsId == null) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}

		try {
			StockNewsResponseDto.NewsInfoDto result = stockNewsRepository.getNewsByNewsId(stockProductId, newsId);

			if (result == null) {
				throw new BusinessException(ErrorCode.INVESTMENT_PRODUCT_NOT_FOUND);
			}

			return result;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}
	}

	/* 만약 주식의 종류가 많아진다면 병렬 처리를 도입해 성능 향상 도모 가능 */

	/**
	 * 9시에 모든 주식에 대한 공시 정보 자동 생성 메서드
	 * schedule은 파라미터를 가질 수 없음
	 */
	@Scheduled(cron = "0 0 9 * * Mon-Fri")
	@Transactional
	@Override
	public void createMorningNews() {
		log.info("09:00 주식 공시 정보 생성 스케줄 작업 시작, hour: {}", LocalDateTime.now().getHour());

		try {
			List<StockProduct> stockProducts = stockProductRepository.findAll();
			for (StockProduct stockProduct : stockProducts) {
				try {
					createNews(stockProduct.getId(), 9);
					log.info("주식 ID: {}에 대한 오전 공시 정보 성공적으로 생성됨", stockProduct.getId());
				} catch (Exception e) {
					log.warn("주식 ID: {}에 대한 오전 공시 정보 생성 실패: {}", stockProduct.getId(), e.getMessage());
				}
			}
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
		} finally {
			log.info("09:00 주식 공시 정보 생성 스케줄 작업 종료, hour: {}", LocalDateTime.now().getHour());
		}

	}

	/**
	 * 13시에 모든 주식에 대한 공시 정보 자동 생성 메서드
	 * schedule은 파라미터를 가질 수 없음
	 */
	@Scheduled(cron = "0 0 13 * * Mon-Fri")
	@Transactional
	@Override
	public void createAfternoonNews() {
		log.info("13:00 주식 공시 정보 생성 스케줄 작업 시작, hour: {}", LocalDateTime.now().getHour());
		try {
			List<StockProduct> stockProducts = stockProductRepository.findAll();
			for (StockProduct stockProduct : stockProducts) {
				try {
					createNews(stockProduct.getId(), 13);
					log.info("주식 ID: {}에 대한 오후 공시 정보 성공적으로 생성됨", stockProduct.getId());
				} catch (Exception e) {
					log.warn("주식 ID: {}에 대한 오후 공시 정보 생성 실패: {}", stockProduct.getId(), e.getMessage());
				}
			}
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
		} finally {
			log.info("13:00 주식 공시 정보 생성 스케줄 작업 종료, hour: {}", LocalDateTime.now().getHour());
		}
	}

	/**
	 * 수동으로 주식 공시 정보를 생성하는 메서드
	 * @param stockProductId
	 * @param hour
	 */
	@Transactional
	@Override
	public StockNewsResponseDto.NewsCreateUpdateDto createNews(
		Integer stockProductId,
		Integer hour
	) {
		log.info("특정 주식 공시 정보 생성 요청: stockProductId = {}, LocalDateTime = {}", stockProductId,
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

		// db에서 시작 시간과 끝 시간 사이에 이미 stockProductId에 해당하는 공시 정보가 존재하는 지 확인
		boolean existsInTimeRange = stockNewsRepository.existsByStockProductIdAndCreatedAtBetween(
			stockProductId, startTime, endTime);

		if (existsInTimeRange) {
			log.warn("해당 시간대에 이미 생성된 뉴스가 있습니다: stockProductId = {}, hour = {}",
				stockProductId, hour);
			throw new BusinessException(ErrorCode.DUPLICATE_NEWS);
		}

		try {

			// stockProductId로 stockProduct 객체 조회
			StockProduct stockProduct = stockProductRepository.findById(stockProductId)
				.orElseThrow(() -> new BusinessException(ErrorCode.INVESTMENT_PRODUCT_NOT_FOUND));

			// product에 대한  <- portfolio에서 productId에 대해 조회한 후 모든 total끼리 합쳐서 계산
			List<StockPortfolio> portfolios = stockPortfolioRepository.findByStockProductId(stockProductId);

			int totalQuantity = 0;
			int totalPurchaseAmount = 0;
			int totalSellAmount = 0;

			for (StockPortfolio portfolio : portfolios) {
				totalQuantity += portfolio.getQuantity();
				totalPurchaseAmount += portfolio.getTotalPurchaseAmount();
				totalSellAmount += portfolio.getTotalSellAmount();
			}

			String prompt = StockAiPrompt.newsPromptTemplate(stockProduct, totalQuantity, totalPurchaseAmount,
				totalSellAmount);
			log.info("AI 공시 정보 생성 요청: prompt = {}", prompt.length());

			String newsContent = stockAiService.generateResponse(prompt);

			log.info("AI 공시 정보 생성 요청: newsContent = {}", newsContent);

			if (newsContent == null) {
				throw new BusinessException(ErrorCode.GENERATE_FAILED);
			}

			// 생성형 AI가 제공해준 정보를 request Dto로 가공
			StockNewsRequestDto.NewsInfoDto request = new StockNewsRequestDto.NewsInfoDto(newsContent);

			// StockNews 엔티티 생성
			StockNews news = StockNews.create(
				stockProduct,
				request.content()
			);

			StockNews savedNews = stockNewsRepository.save(news);

			return new StockNewsResponseDto.NewsCreateUpdateDto(savedNews.getId());
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}

	}

	@Transactional
	@Override
	public StockNewsResponseDto.NewsCreateUpdateDto updateNews(StockNewsRequestDto.NewsInfoDto request,
		Integer stockProductId, Integer newsId) {
		log.info("특정 공시 정보 수정 요청: requestDto = {}, stockProductId = {}, newsId = {}", request, stockProductId, newsId);

		if (stockProductId == null || newsId == null || request == null) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}

		try {
			StockNews news = stockNewsRepository.findById(newsId)
				.orElseThrow(() -> new BusinessException(ErrorCode.INVESTMENT_PRODUCT_NOT_FOUND));

			/* JPA 더티 체킹으로 객체에 먼저 변경 사항이 캐시된 후, transaction이 끝날 때 자동으로 쿼리를 통해 DB를 업데이트 */
			news.modifyContent(request.content());

			return StockNewsResponseDto.NewsCreateUpdateDto.stockNewsEntityToNewsCreateUpdateDto(news);
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}
	}

}
