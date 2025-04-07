package com.lufin.server.stock.service;

public interface StockPriceHistoryService {
	// 주식 가격 변동 스케줄링
	void updateMorningStockPrice();

	void updateAfternoonStockPrice();

	// 주식 가격 수동 변동
	void updateStockPrice(Integer stockProductId, Integer hour);
}
