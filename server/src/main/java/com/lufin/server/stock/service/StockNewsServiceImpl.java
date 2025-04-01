package com.lufin.server.stock.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.stock.domain.StockNews;
import com.lufin.server.stock.domain.StockProduct;
import com.lufin.server.stock.dto.StockNewsRequestDto;
import com.lufin.server.stock.dto.StockNewsResponseDto;
import com.lufin.server.stock.repository.StockNewsRepository;
import com.lufin.server.stock.repository.StockProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockNewsServiceImpl implements StockNewsService {
	private final StockNewsRepository stockNewsRepository;
	private final StockProductRepository stockProductRepository;

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

			return result;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}
	}

	/**
	 * 특정 주식 공시 정보 생성
	 * 공시되는 시각에 생성
	 * @param stockProductId
	 */
	@Scheduled(cron = "0 0 9 * * Mon-Fri")
	@Override
	public StockNewsResponseDto.NewsCreateUpdateDto createMorningNews(Integer stockProductId) {
		return createNews(stockProductId, 9);
	}

	@Scheduled(cron = "0 0 13 * * Mon-Fri")
	@Override
	public StockNewsResponseDto.NewsCreateUpdateDto createAfternoonNews(Integer stockProductId) {
		return createNews(stockProductId, 13);
	}

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

			// TODO: 생성형 AI API를 활용해 공시 정보 생성 후 newsContent에 대입
			String newsContent = "테스트용트스테";

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

}
