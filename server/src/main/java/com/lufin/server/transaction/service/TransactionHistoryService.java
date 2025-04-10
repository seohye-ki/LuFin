package com.lufin.server.transaction.service;

import java.time.LocalDate;

import com.lufin.server.account.domain.Account;
import com.lufin.server.common.constants.HistoryStatus;
import com.lufin.server.member.domain.Member;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.domain.TransactionType;

public interface TransactionHistoryService {

	/**
	 * 거래 발생 시 거래 이력을 기록합니다.
	 * <p>
	 * 실행자의 역할(MemberRole)은 executor 객체에서 자동 추출되어 저장됩니다.
	 * @param fromAccount 출금 계좌
	 * @param toAccountNumber 입금 계좌 번호
	 * @param executor 거래 실행자 (교사 또는 학생)
	 * @param amount 거래 금액
	 * @param balanceAfter 거래 후 잔액 (nullable)
	 * @param type 거래 유형
	 * @param status 거래 상태
	 * @param description 거래 설명 또는 메모
	 * @param sourceType 거래 발생 출처
	 */
	void record(
		Account fromAccount,
		String toAccountNumber,
		Member executor,
		int amount,
		int balanceAfter,
		TransactionType type,
		HistoryStatus status,
		String description,
		TransactionSourceType sourceType
	);

	// 지난주 대비 소비 증감량을 반환 (이번 주 - 지난 주)
	int getTotalConsumptionBetween(int memberId, int classId, LocalDate from, LocalDate to);

	int getTotalAccumulatedConsumption(int studentId, int classId);
}
