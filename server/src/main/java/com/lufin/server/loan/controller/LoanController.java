package com.lufin.server.loan.controller;

import static com.lufin.server.common.utils.ValidationUtils.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.loan.dto.LoanApplicationRequestDto;
import com.lufin.server.loan.dto.LoanApplicationResponseDto;
import com.lufin.server.loan.service.LoanService;
import com.lufin.server.member.support.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lufin/loans")
public class LoanController {
	private static final String CLASS_ID = "classId";

	private final LoanService loanService;

	@PostMapping("/applications")
	public ResponseEntity<ApiResponse<LoanApplicationResponseDto>> createLoanApplication(HttpServletRequest httpRequest, @RequestBody @Valid LoanApplicationRequestDto request) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		LoanApplicationResponseDto result = loanService.createLoanApplication(request, UserContext.get(), classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}
}