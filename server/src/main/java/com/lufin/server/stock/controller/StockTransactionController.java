package com.lufin.server.stock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.stock.dto.StockTransactionResponseDto;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/stocks/transactions")
@RequiredArgsConstructor
public class StockTransactionController {
	@PostMapping("/{productId}")
	public ResponseEntity<ApiResponse<StockTransactionResponseDto.TransactionInfoDto>> buyStock(
		@PathVariable @Positive Integer productId
	) {
		// TODO: null -> dto
		return ResponseEntity.status(201).body(ApiResponse.success(null));
	}

}
