package com.lufin.server.loan.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lufin.server.loan.domain.LoanApplication;
import com.lufin.server.loan.domain.LoanApplicationStatus;

public record LoanApplicationListDto(
	Integer loanApplicationId,
	Integer memberId,
	String memberName,
	String productName,
	Integer requiredAmount,
	LoanApplicationStatus status,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime dueDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime startedAt
) {

	public static LoanApplicationListDto from(LoanApplication application) {
		return new LoanApplicationListDto(
			application.getId(),
			application.getMember().getId(),
			application.getMember().getName(),
			application.getLoanProduct().getName(),
			application.getRequiredAmount(),
			application.getStatus(),
			application.getDueDate(),
			application.getStartedAt()
		);
	}

}
