package com.lufin.server.loan.dto;

import com.lufin.server.loan.domain.LoanApplication;
import com.lufin.server.loan.domain.LoanApplicationStatus;

public record LoanApplicationListDto(
	Integer loanApplicationId,
	Integer memberId,
	String memberName,
	String productName,
	Integer requiredAmount,
	LoanApplicationStatus status
) {

	public static LoanApplicationListDto from(LoanApplication application) {
		return new LoanApplicationListDto(
			application.getId(),
			application.getMember().getId(),
			application.getMember().getName(),
			application.getLoanProduct().getName(),
			application.getRequiredAmount(),
			application.getStatus()
		);
	}

}
