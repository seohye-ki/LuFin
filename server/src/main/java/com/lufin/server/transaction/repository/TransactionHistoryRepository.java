package com.lufin.server.transaction.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lufin.server.transaction.domain.TransactionHistory;
import com.lufin.server.transaction.domain.TransactionSourceType;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

	// 특정 계좌에서 기준일이전에 발생한 거래 중 가장 마지막 거래 기록 조회 (최신 기록 조회)
	Optional<TransactionHistory> findTopByFromAccount_IdAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
		int accountId, LocalDateTime dateTime);

	// 특정 학생이 실행한 특정 유형의 거래 중, 기준일 이전 가장 최근 1건 조회 (대출)
	Optional<TransactionHistory> findTopByExecutor_IdAndSourceTypeAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
		int memberId, TransactionSourceType transactionSourceType, LocalDateTime localDateTime);

	// 클래스 계좌로 보내는 거래만 계산 (소비)
	@Query("""
    SELECT SUM(th.amount)
    FROM TransactionHistory th
    JOIN Account fromAcc ON th.fromAccount.id = fromAcc.id
    JOIN Account toAcc ON th.toAccountNumber = toAcc.accountNumber
    WHERE fromAcc.member.id = :memberId
      AND fromAcc.classroom.id = :classroomId
      AND toAcc.member.id IS NULL
      AND th.transactionType IN :types
      AND th.status = 'SUCCESS'
      AND th.createdAt BETWEEN :from AND :to
""")
	Integer sumConsumptionAmountByMemberAndDateRange(
		@Param("memberId") int memberId,
		@Param("classroomId") int classroomId,
		@Param("types") List<TransactionSourceType> types,
		@Param("from") LocalDateTime from,
		@Param("to") LocalDateTime to
	);
}
