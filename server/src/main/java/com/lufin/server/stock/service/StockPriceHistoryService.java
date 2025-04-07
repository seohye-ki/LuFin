package com.lufin.server.stock.service;

import java.util.List;

import com.lufin.server.stock.dto.StockPriceHistoryResponseDto;

public interface StockPriceHistoryService {
	// 주식 가격 변동 조회
	List<StockPriceHistoryResponseDto.PriceHistoryResponseDto> getStockPriceHistory(Integer stockProductId,
		Integer counts);

	// 주식 가격 변동 스케줄링
	void updateMorningStockPrice();

	void updateAfternoonStockPrice();

	// 주식 가격 수동 변동
	void updateStockPrice(Integer stockProductId, Integer hour);
}
