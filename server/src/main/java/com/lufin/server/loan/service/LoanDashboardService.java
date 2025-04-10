package com.lufin.server.loan.service;

public interface LoanDashboardService {

	// [학생] 해당 학생이 특정 반에서 진행 중인 대출 원금을 반환 (없으면 0)
	int getLoanPrincipal(int memberId, int classId);

	// [클래스] 대출 총 원금 조회
	long getTotalClassLoanPrincipal(int classId);
}
