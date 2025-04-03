package com.lufin.server.loan.dto;

import com.lufin.server.loan.domain.LoanApplicationStatus;

import jakarta.validation.constraints.NotNull;

public record LoanApplicationApprovalDto(
	@NotNull
	LoanApplicationStatus status
) {
}
