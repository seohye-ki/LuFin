package com.lufin.server.stock.controller;

import java.time.LocalTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.stock.dto.StockNewsRequestDto;
import com.lufin.server.stock.dto.StockNewsResponseDto;
import com.lufin.server.stock.service.StockNewsService;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/stocks/products/{productId}/news")
@RequiredArgsConstructor
public class StockNewsController {
	private final StockNewsService stockNewsService;

	/**
	 * 공시 정보 목록 조회
	 * @param productId
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<List<StockNewsResponseDto.NewsInfoDto>>> getAllNews(
		@PathVariable @PositiveOrZero Integer productId
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
			return ResponseEntity.status(500).body(ApiResponse.failure(ErrorCode.SERVER_ERROR));
		}

		return ResponseEntity.status(200).body(ApiResponse.success(result));
	}

	/**
	 * 특정 주식 공시 정보 생성(수동)
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<StockNewsResponseDto.NewsCreateUpdateDto>> createNews(
		@PathVariable @Positive Integer productId
	) {
		StockNewsResponseDto.NewsCreateUpdateDto result;
		int currentHour = LocalTime.now().getHour();

		result = stockNewsService.createNews(productId, currentHour);

		return ResponseEntity.status(201).body(ApiResponse.success(result));
	}

	/**
	 * 특정 주식 공시 정보 수정
	 */
	@PutMapping("/{newsId}")
	public ResponseEntity<ApiResponse<StockNewsResponseDto.NewsCreateUpdateDto>> updateNews(
		@PathVariable @Positive Integer productId,
		@PathVariable @Positive Integer newsId,
		@RequestBody StockNewsRequestDto.NewsInfoDto request
	) {
		StockNewsResponseDto.NewsCreateUpdateDto result = stockNewsService.updateNews(request, productId, newsId);

		if (result == null) {
			return ResponseEntity.status(500).body(ApiResponse.failure(ErrorCode.SERVER_ERROR));
		}

		return ResponseEntity.status(200).body(ApiResponse.success(result));
	}

}
