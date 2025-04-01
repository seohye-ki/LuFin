package com.lufin.server.stock.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.stock.dto.StockNewsResponseDto;
import com.lufin.server.stock.service.StockNewsService;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/stocks/products/{productId}/news")
@RequiredArgsConstructor
public class StockNewsController {
	private final StockNewsService stockNewsService;

	/**
	 * 특정 주식 공시 정보 목록 조회
	 * @param productId
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<List<StockNewsResponseDto.NewsInfoDto>>> getAllNews(
		@PathVariable @Positive Integer productId
	) {
		List<StockNewsResponseDto.NewsInfoDto> result = stockNewsService.getAllNews(productId);

		return ResponseEntity.status(200).body(ApiResponse.success(result));
	}

	/**
	 * 특정 주식 공시 정보 상세 조회
	 * @param productId
	 * @param newsId
	 */
	@GetMapping("/{newsId}")
	public ResponseEntity<ApiResponse<StockNewsResponseDto.NewsInfoDto>> getNewsByNewsId(
		@PathVariable @Positive Integer productId,
		@PathVariable @Positive Integer newsId
	) {
		StockNewsResponseDto.NewsInfoDto result = stockNewsService.getNewsByNewsId(productId, newsId);

		if (result == null) {
			return ResponseEntity.status(404).body(ApiResponse.failure(ErrorCode.INVESTMENT_PRODUCT_NOT_FOUND));
		}

		return ResponseEntity.status(200).body(ApiResponse.success(result));
	}
}
