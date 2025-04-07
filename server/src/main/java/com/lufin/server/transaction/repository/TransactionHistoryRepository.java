package com.lufin.server.transaction.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.transaction.domain.TransactionHistory;
import com.lufin.server.transaction.domain.TransactionSourceType;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

	// 해당 회원이 실행한 거래 내역 중, 특정 기간 내의 거래를 모두 조회
	List<TransactionHistory> findAllByExecutorIdAndCreatedAtBetween(
		int executorId, LocalDateTime from, LocalDateTime to
	);

	// 특정 계좌에서 기준일이전에 발생한 거래 중 가장 마지막 거래 기록 조회 (최신 기록 조회)
	Optional<TransactionHistory> findTopByFromAccount_IdAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
		int accountId, LocalDateTime dateTime);

	// 기준일 하루 동안의 투자 구매 거래 조회
	List<TransactionHistory> findAllByExecutorIdAndSourceTypeAndCreatedAtBetween(
		int executorId,
		TransactionSourceType sourceType,
		LocalDateTime from,
		LocalDateTime to
	);

	// 특정 학생이 실행한 특정 유형의 거래 중, 기준일 이전 가장 최근 1건 조회 (대출)
	Optional<TransactionHistory> findTopByExecutor_IdAndSourceTypeAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
		int memberId, TransactionSourceType transactionSourceType, LocalDateTime localDateTime);
}
