package com.lufin.server.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.lufin.server.loan.domain.LoanApplication;
import com.lufin.server.loan.domain.LoanApplicationStatus;

public record LoanApplicationDetailDto(
	Integer loanApplicationId,
	Integer memberId,
	String memberName,
	Integer loanProductId,
	String loanProductName,
	String description,
	LoanApplicationStatus status,
	Integer requiredAmount,
	Integer interestAmount,
	BigDecimal interestRate,
	Integer overdueCount,
	LocalDateTime nextPaymentDate,
	LocalDateTime createdAt,
	LocalDateTime startedAt,
	LocalDateTime dueDate
) {

	public static LoanApplicationDetailDto from(LoanApplication loanApplication) {
		return new LoanApplicationDetailDto(
			loanApplication.getId(),
			loanApplication.getMember().getId(),
			loanApplication.getMember().getName(),
			loanApplication.getLoanProduct().getId(),
			loanApplication.getLoanProduct().getName(),
			loanApplication.getDescription(),
			loanApplication.getStatus(),
			loanApplication.getRequiredAmount(),
			loanApplication.getInterestAmount(),
			loanApplication.getLoanProduct().getInterestRate(),
			loanApplication.getOverdueCount(),
			loanApplication.getNextPaymentDate(),
			loanApplication.getCreatedAt(),
			loanApplication.getStartedAt(),
			loanApplication.getDueDate()
		);
	}
}
