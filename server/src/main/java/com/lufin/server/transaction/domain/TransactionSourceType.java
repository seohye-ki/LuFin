package com.lufin.server.transaction.domain;

public enum TransactionSourceType {
	DEPOSIT,            // 일반 입금
	WITHDRAWAL,         // 일반 출금
	TRANSFER,           // 계좌 간 이체
	LOAN_DISBURSEMENT,  // 대출 실행
	LOAN_PRINCIPAL_REPAYMENT,     // 대출 원금 상환
	LOAN_INTEREST_REPAYMENT,      // 대출 이자 상환
	INVESTMENT_BUY,     // 투자 매수
	INVESTMENT_SELL,    // 투자 매도
	ITEM_PURCHASE,      // 상점 구매
	REFUND              // 환불
}
