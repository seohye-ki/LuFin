package com.lufin.server.stock.service;

import java.util.List;

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
	 * 공시되기 n분 전에 자동으로 생성될 필요 있음
	 * @param stockProductId
	 * @param newsInfoDto
	 */
	@Override
	public StockNewsResponseDto.NewsCreateUpdateDto createNews(Integer stockProductId,
		StockNewsRequestDto.NewsInfoDto newsInfoDto) {
		log.info("특정 주식 공시 정보 생성 요청: stockProductId = {}, newsInfoDto = {}", stockProductId, newsInfoDto);

		if (stockProductId == null || newsInfoDto == null) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}

		try {
			// stockProductId로 stockProduct 객체 조회
			StockProduct stockProduct = stockProductRepository.findById(stockProductId)
				.orElseThrow(() -> new BusinessException(ErrorCode.INVESTMENT_PRODUCT_NOT_FOUND));

			// TODO: 생성형 AI API를 활용해 공시 정보 생성 후 newsContent에 대입
			String newsContent = "테스트용트스테";
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
