package com.lufin.server.loan.controller;

import static com.lufin.server.common.utils.ValidationUtils.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.loan.dto.LoanApplicationListDto;
import com.lufin.server.loan.dto.LoanApplicationRequestDto;
import com.lufin.server.loan.dto.LoanApplicationDetailDto;
import com.lufin.server.loan.dto.MyLoanApplicationDto;
import com.lufin.server.loan.service.LoanService;
import com.lufin.server.member.domain.Member;
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

	// 대출 신청
	@PostMapping("/applications")
	public ResponseEntity<ApiResponse<LoanApplicationDetailDto>> createLoanApplication(HttpServletRequest httpRequest, @RequestBody @Valid LoanApplicationRequestDto request) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		Member member = UserContext.get();
		LoanApplicationDetailDto result = loanService.createLoanApplication(request, member, classId);
		return ResponseEntity.status(201).body(ApiResponse.success(result));
	}

	// 대출 내역 목록 조회
	@GetMapping("/applications")
	public ResponseEntity<ApiResponse<List<LoanApplicationListDto>>> getLoanApplications(HttpServletRequest httpRequest) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		Member member = UserContext.get();
		List<LoanApplicationListDto> result = loanService.getLoanApplications(member, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 나의 현재 활성화된 대출 조회
	@GetMapping("/my-application")
	public ResponseEntity<ApiResponse<MyLoanApplicationDto>> getActiveLoanApplication(HttpServletRequest httpRequest) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		Member member = UserContext.get();
		MyLoanApplicationDto result = loanService.getActiveLoanApplication(member, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

}