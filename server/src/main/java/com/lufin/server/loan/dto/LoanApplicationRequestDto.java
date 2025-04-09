package com.lufin.server.loan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LoanApplicationRequestDto(
	@NotNull
	@Positive
	Integer loanProductId,

	@NotNull
	@Positive
	Integer requestedAmount,

	@NotBlank
	String description
) {
}
