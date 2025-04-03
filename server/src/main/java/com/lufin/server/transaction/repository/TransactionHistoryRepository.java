package com.lufin.server.transaction.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.transaction.domain.TransactionHistory;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

	// 해당 회원이 실행한 거래 내역 중, 특정 기간 내의 거래를 모두 조회
	List<TransactionHistory> findAllByExecutorIdAndCreatedAtBetween(
		int executorId, LocalDateTime from, LocalDateTime to
	);
}
