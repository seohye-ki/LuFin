package com.lufin.server.account.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lufin.server.account.domain.TransactionHistory;
import com.lufin.server.common.constants.HistoryStatus;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

	// 출금 계좌 ID로 거래 내역 조회
	List<TransactionHistory> findByFromAccountId(Integer fromAccountId);

	// 실행자(멤버 ID) 기준으로 거래 내역 조회
	List<TransactionHistory> findByExecutorId(Integer memberId);

	// 거래 상태(status)로 필터링된 거래 내역 조회
	List<TransactionHistory> findByStatus(HistoryStatus status);

	// 출금 계좌 기준으로 최근 거래 10건 조회 (최신순)
	List<TransactionHistory> findTop10ByFromAccountIdOrderByCreatedAtDesc(Integer fromAccountId);

	// 특정 계좌의 특정 기간 동안의 거래 내역 조회
	@Query("""
		    SELECT t FROM TransactionHistory t
		    WHERE t.fromAccount.id = :accountId
		    AND t.createdAt BETWEEN :start AND :end
		""")
	List<TransactionHistory> findByAccountAndDateRange(
		@Param("accountId") int accountId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);
}
