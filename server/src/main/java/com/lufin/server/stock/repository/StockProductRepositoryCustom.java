package com.lufin.server.stock.repository;

import java.util.List;

import com.lufin.server.stock.dto.StockResponseDto;

public interface StockProductRepositoryCustom {
	// 주식 상품 목록 전체 조회
	List<StockResponseDto.StockInfoDto> getAllStocks();

	// 주식 상품 상세 조회
	StockResponseDto.StockInfoDto getStock(Integer stockProductId);
}
