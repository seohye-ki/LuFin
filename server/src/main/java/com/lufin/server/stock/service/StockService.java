package com.lufin.server.stock.service;

import java.time.LocalDate;

public interface StockService {

	// [학생] 해당 학생이 보유한 주식의 총 평가 금액
	int getTotalValuation(int memberId, int classId);

	// [학생] 날짜 기준 보유 주식 평가액 조회
	int getTotalValuationOnDate(int memberId, int classId, LocalDate date);

	// 각 주식의 현재 보유 개수와 그 주식의 현재 가치 조회하고 총 평가 금액 반환
	int getTotalValuationByStocks(int memberId, int classId);
	
}
