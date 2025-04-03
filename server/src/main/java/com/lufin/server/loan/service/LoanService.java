package com.lufin.server.loan.service;

import java.util.List;

import com.lufin.server.loan.dto.LoanApplicationListDto;
import com.lufin.server.loan.dto.LoanApplicationRequestDto;
import com.lufin.server.loan.dto.LoanApplicationDetailDto;
import com.lufin.server.loan.dto.LoanProductResponseDto;
import com.lufin.server.member.domain.Member;

import jakarta.validation.Valid;

public interface LoanService {

	List<LoanProductResponseDto> getLoanProducts(Member member);

	LoanApplicationDetailDto createLoanApplication(@Valid LoanApplicationRequestDto request, Member member, Integer classId);

	List<LoanApplicationListDto> getLoanApplications(Member member, Integer classId);

	LoanApplicationDetailDto getLoanApplicationDetail(Integer loanApplicationId, Member member, Integer classId);
}
