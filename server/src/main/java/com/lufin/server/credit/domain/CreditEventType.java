package com.lufin.server.credit.domain;

public enum CreditEventType {
	LOAN_PRINCIPLE_REPAYMENT("대출 원금 상환"),
	LOAN_PRINCIPLE_DEFAULT("대출 원금 연체"),
	LOAN_INTEREST_REPAYMENT("대출 이자 상환"),
	LOAN_INTEREST_DEFAULT("대출 이자 연체"),
	MISSION_COMPLETION("미션 수행 완료"),
	MISSION_FAILURE("미션 수행 실패"),
	SYSTEM_RECOVERY("회생 시스템");

	private final String displayName;

	CreditEventType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
