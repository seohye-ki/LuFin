package com.lufin.server.stock.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.common.utils.ValidationUtils;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;
import com.lufin.server.stock.dto.StockPortfolioResponseDto;
import com.lufin.server.stock.service.StockPortfolioService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/stocks/portfolios")
@RequiredArgsConstructor
public class StockPortfolioController {
	private final StockPortfolioService stockPortfolioService;

	/**
	 * 특정 유저의 포트폴리오 조회
	 * @param httpRequest
	 * @return
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<List<StockPortfolioResponseDto.PortfolioInfoDto>>> getPortfolio(
		HttpServletRequest httpRequest
	) {
		Integer classId = (Integer)httpRequest.getAttribute("classId");
		ValidationUtils.validateClassId(classId);

		Member currentMember = UserContext.get();

		List<StockPortfolioResponseDto.PortfolioInfoDto> response = stockPortfolioService.getPortfolio(
			currentMember,
			classId
		);

		return ResponseEntity.status(200).body(ApiResponse.success(response));

	}
}
