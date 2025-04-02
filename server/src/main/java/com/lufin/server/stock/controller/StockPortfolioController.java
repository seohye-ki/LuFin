package com.lufin.server.stock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.common.utils.ValidationUtils;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/stocks/portfolios")
@RequiredArgsConstructor
public class StockPortfolioController {
	/**
	 * 특정 유저의 포트폴리오 조회
	 * @param httpRequest
	 * @return
	 */
	@GetMapping
	public ResponseEntity<ApiResponse> getPortfolio(
		HttpServletRequest httpRequest
	) {
		Integer classId = (Integer)httpRequest.getAttribute("classId");
		ValidationUtils.validateClassId(classId);

		Member currentMember = UserContext.get();

		try {
			// TODO: null -> dto
			return null;
		} catch (Exception e) {
			return ResponseEntity.status(500).body(ApiResponse.failure(ErrorCode.SERVER_ERROR));
		}
	}
}
