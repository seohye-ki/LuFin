package com.lufin.server.stock.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/stocks/transactions")
@RequiredArgsConstructor
public class StockTransactionController {
	private final StockTransactionService stockTransactionService;

	/**
	 * 주식 거래
	 * @param productId
	 * @param request
	 * @param httpRequest
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/{productId}")
	public ResponseEntity<ApiResponse<StockTransactionResponseDto.TransactionInfoDto>> transactStock(
		@PathVariable @Positive Integer productId,
		@RequestBody @Valid StockTransactionRequestDto.TransactionInfoDto request,
		HttpServletRequest httpRequest,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(400).body(ApiResponse.failure(ErrorCode.INVALID_INPUT_VALUE));
		}

		Integer classId = (Integer)httpRequest.getAttribute("classId");
		ValidationUtils.validateClassId(classId);

		Member currentMember = UserContext.get();

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

	// 특정 유저의 전체 주식 내역 조회
	@GetMapping
	public ResponseEntity<ApiResponse<List>> getAllTransactionByMemberId(
		HttpServletRequest httpRequest
	) {
		//TODO null -> dto
		return null;
	}
}
