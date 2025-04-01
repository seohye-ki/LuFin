package com.lufin.server.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.stock.dto.StockNewsResponseDto;
import com.lufin.server.stock.repository.StockRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockNewsServiceImpl implements StockNewsService {
	private final StockRepository stockRepository;

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
			List<StockNewsResponseDto.NewsInfoDto> result = stockRepository.getAllNews(stockProductId);

			return null;
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
			StockNewsResponseDto.NewsInfoDto result = stockRepository.getNewsByNewsId(stockProductId, newsId);

			return result;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}
	}

}
