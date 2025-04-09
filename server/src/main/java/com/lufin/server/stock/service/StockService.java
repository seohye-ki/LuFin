package com.lufin.server.stock.service;

import java.time.LocalDate;

public interface StockService {

	// [학생] 해당 학생이 보유한 주식의 총 평가 금액
	int getTotalValuation(int memberId, int classId);

	// [학생] 특정 날짜의 투자 금액 총합 (주식 구매 등)
	int getInvestmentAmountOnDate(int memberId, LocalDate date);

	// [클래스] 투자 일일 합계 조회
	long getTotalClassInvestmentOnDate(int classId, LocalDate date);

	// [클래스] 투자 합계
	long getTotalClassInvestment(int classId);
}
