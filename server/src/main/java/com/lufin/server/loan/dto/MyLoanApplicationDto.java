package com.lufin.server.loan.dto;

import java.time.LocalDateTime;

import com.lufin.server.loan.domain.LoanApplication;

public record MyLoanApplicationDto(
	String productName, //상품명
	Integer requiredAmount, // 대출액
	boolean principalOverdue, // 원금 연체여부
	Integer nextPrincipalAmount, // 이번에 낼 원금액
	Integer overdueCount, // 이자 연체 횟수
	Integer nextInterestAmount, //이번에 낼 이자액
	Integer currentCount, // 현재 납부 횟수
	Integer totalCount, // 전체 납부 횟수
	LocalDateTime dueDate // 원금 납부 만가일
) {

	public static MyLoanApplicationDto from(LoanApplication application) {
		return new MyLoanApplicationDto(
			application.getLoanProduct().getName(),
			application.getRequiredAmount(),
			application.isPrincipalOverdue(),
			application.calculateNextPrincipalAmount(),
			application.getOverdueCount(),
			application.calculateNextInterestAmount(),
			application.calculateCurrentCount(),
			application.calculateTotalCount(),
			application.getDueDate()
		);
	}

}
