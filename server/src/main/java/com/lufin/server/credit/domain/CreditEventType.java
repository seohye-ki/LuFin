package com.lufin.server.credit.domain;

public enum CreditEventType {
	LOAN_PRINCIPLE_REPAYMENT("대출 원금 상환"),
	LOAN_PRINCIPLE_DEFAULT("대출 원금 연체"),
	LOAN_INTEREST_REPAYMENT("대출 이자 상환"),
	LOAN_INTEREST_DEFAULT("대출 이자 연체"),
	MISSION_COMPLETION("미션 수행"),
	MISSION_FAILURE("미션 수행"),
	INVESTMENT_PROFIT("투자 수익"),
	INVESTMENT_LOSS("투자 손실"),
	SYSTEM_RECOVERY("회생 시스템");

	private final String displayName;

	CreditEventType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
