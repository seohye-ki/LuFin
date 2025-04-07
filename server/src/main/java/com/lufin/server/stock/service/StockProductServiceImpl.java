package com.lufin.server.stock.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.stock.dto.StockResponseDto;
import com.lufin.server.stock.repository.StockProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockProductServiceImpl implements StockProductService {
	private final StockProductRepository stockProductRepository;

	/**
	 * 주식 상품 목록 조회
	 */
	@Override
	public List<StockResponseDto.StockInfoDto> getAllStocks() {
		log.info("주식 상품 전체 조회 요청: ");

		try {
			List<StockResponseDto.StockInfoDto> result = stockProductRepository.getAllStocks();

			return result;

		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	/**
	 * 주식 상품 상세 조회
	 * @param stockProductId
	 */
	@Override
	public StockResponseDto.StockInfoDto getStock(Integer stockProductId) {
		log.info("주식 상품 상세 조회 요청: stockProductId = {}", stockProductId);

		if (stockProductId == null) {
			throw new BusinessException(INVALID_INPUT_VALUE);
		}

		try {
			StockResponseDto.StockInfoDto result = stockProductRepository.getStock(stockProductId);

			return result;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Scheduled(cron = "0 0 10 * * Mon-Fri")
	@Transactional
	@Override
	public void updateMorningStockPrice() {
		log.info("10:00 주식 가격 업데이트 스케줄 작업 시작: hour = {}", LocalDateTime.now().getHour());
		try {
			List<StockResponseDto.StockInfoDto> stocks = stockProductRepository.getAllStocks();
			for (StockResponseDto.StockInfoDto stock : stocks) {
				updateStockPrice(stock.stockProductId(), stock.currentPrice());
			}

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Scheduled(cron = "0 0 14 * * Mon-Fri")
	@Transactional
	@Override
	public void updateAfternoonStockPrice() {
		log.info("14:00 주식 가격 업데이트 스케줄 작업 시작: hour = {}", LocalDateTime.now().getHour());
		try {
			List<StockResponseDto.StockInfoDto> stocks = stockProductRepository.getAllStocks();
			for (StockResponseDto.StockInfoDto stock : stocks) {
				updateStockPrice(stock.stockProductId(), stock.currentPrice());
			}

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Override
	public void updateStockPrice(Integer stockProductId, Integer price) {
		
	}
}
