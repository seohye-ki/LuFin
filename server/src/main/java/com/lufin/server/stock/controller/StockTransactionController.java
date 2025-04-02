package com.lufin.server.stock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.common.utils.ValidationUtils;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;
import com.lufin.server.stock.dto.StockTransactionRequestDto;
import com.lufin.server.stock.dto.StockTransactionResponseDto;
import com.lufin.server.stock.service.StockTransactionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/stocks/transactions")
@RequiredArgsConstructor
public class StockTransactionController {
	private final StockTransactionService stockTransactionService;

	@PostMapping("/{productId}")
	public ResponseEntity<ApiResponse<StockTransactionResponseDto.TransactionInfoDto>> transactStock(
		@PathVariable @Positive Integer productId,
		@RequestBody StockTransactionRequestDto.TransactionInfoDto request,
		HttpServletRequest httpRequest
	) {
		Integer classId = (Integer)httpRequest.getAttribute("classId");
		ValidationUtils.validateClassId(classId);

		Member currentMember = UserContext.get();

		//TODO: 2차 비밀번호 검증 로직 필요

		StockTransactionResponseDto.TransactionInfoDto result = stockTransactionService.transactStock(
			request,
			currentMember,
			productId,
			classId
		);

		if (result == null) {
			return ResponseEntity.status(500).body(ApiResponse.failure(ErrorCode.SERVER_ERROR));
		}

		return ResponseEntity.status(201).body(ApiResponse.success(result));
	}

}
