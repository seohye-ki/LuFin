package com.lufin.server.loan.dto;

import java.math.BigDecimal;

import com.lufin.server.loan.domain.LoanProduct;

public record LoanProductResponseDto(
	Integer id,
	String name,
	String description,
	Integer creditRank,
	Integer maxAmount,
	BigDecimal interestRate,
	Integer period
) {
	public static LoanProductResponseDto from(LoanProduct loanProduct) {
		return new LoanProductResponseDto(
			loanProduct.getId(),
			loanProduct.getName(),
			loanProduct.getDescription(),
			loanProduct.getCreditRank(),
			loanProduct.getMaxAmount(),
			loanProduct.getInterestRate(),
			loanProduct.getPeriod()
		);
	}
}
