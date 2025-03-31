package com.lufin.server.loan.service;

import com.lufin.server.loan.dto.LoanApplicationRequestDto;
import com.lufin.server.loan.dto.LoanApplicationResponseDto;
import com.lufin.server.member.domain.Member;

import jakarta.validation.Valid;

public interface LoanService {

	LoanApplicationResponseDto createLoanApplication(@Valid LoanApplicationRequestDto request, Member member, Integer classId);
}
