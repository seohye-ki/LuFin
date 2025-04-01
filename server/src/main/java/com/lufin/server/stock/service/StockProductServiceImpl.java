package com.lufin.server.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.stock.dto.StockResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockProductServiceImpl implements StockProductService {

	/**
	 * 주식 상품 목록 조회
	 */
	@Override
	public List<StockResponseDto.StockInfoDto> getAllStocks() {
		return List.of();
	}

	/**
	 * 주식 상품 상세 조회
	 * @param stockCode
	 */
	@Override
	public StockResponseDto.StockInfoDto getStock(String stockCode) {
		return null;
	}
}
