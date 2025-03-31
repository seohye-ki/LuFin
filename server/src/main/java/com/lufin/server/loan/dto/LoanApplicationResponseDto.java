package com.lufin.server.loan.dto;

import java.time.LocalDateTime;

import com.lufin.server.loan.domain.LoanApplication;
import com.lufin.server.loan.domain.LoanApplicationStatus;

public record LoanApplicationResponseDto(
	Integer loanApplicationId,
	Integer memberId,
	Integer classroomId,
	Integer loanProductId,
	String description,
	LoanApplicationStatus status,
	Integer requiredAmount,
	Integer interestAmount,
	Integer overdueCount,
	LocalDateTime nextPaymentDate,
	LocalDateTime createdAt,
	LocalDateTime startedAt,
	LocalDateTime dueDate
) {
	public static LoanApplicationResponseDto from(LoanApplication loanApplication) {
		return new LoanApplicationResponseDto(
			loanApplication.getId(),
			loanApplication.getMember().getId(),
			loanApplication.getClassroom().getId(),
			loanApplication.getLoanProduct().getId(),
			loanApplication.getDescription(),
			loanApplication.getStatus(),
			loanApplication.getRequiredAmount(),
			loanApplication.getInterestAmount(),
			loanApplication.getOverdueCount(),
			loanApplication.getNextPaymentDate(),
			loanApplication.getCreatedAt(),
			loanApplication.getStartedAt(),
			loanApplication.getDueDate()
		);
	}
}
