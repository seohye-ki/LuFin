package com.lufin.server.stock.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.stock.dto.StockResponseDto;
import com.lufin.server.stock.service.StockProductService;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/stocks/products")
@RequiredArgsConstructor
public class StockProductController {
	private final StockProductService stockProductService;

	/**
	 * 주식 상품 목록 조회
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<List<StockResponseDto.StockInfoDto>>> getAllStocks() {
		List<StockResponseDto.StockInfoDto> result = stockProductService.getAllStocks();

		return ResponseEntity.status(200).body(ApiResponse.success(result));
	}

	/**
	 * 주식 상품 상세 조회
	 */
	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<StockResponseDto.StockInfoDto>> getStock(
		@PathVariable @Positive Integer productId
	) {
		StockResponseDto.StockInfoDto result = stockProductService.getStock(productId);

		if (result == null) {
			return ResponseEntity.status(404).body(ApiResponse.failure(ErrorCode.INVESTMENT_PRODUCT_NOT_FOUND));
		}
		
		return ResponseEntity.status(200).body(ApiResponse.success(result));
	}
}
