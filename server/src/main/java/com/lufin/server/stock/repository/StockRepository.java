package com.lufin.server.stock.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.stock.domain.StockTransactionHistory;

public interface StockRepository extends JpaRepository<StockTransactionHistory, Integer> {

	// 특정 학생의 전체 주식 거래 이력 조회
	List<StockTransactionHistory> findAllByMemberIdAndClassroomId(int memberId, int classId);

	// 특정 날짜 범위 내의 주식 거래 조회
	List<StockTransactionHistory> findAllByMemberIdAndCreatedAtBetween(
		int memberId,
		LocalDateTime start,
		LocalDateTime end
	);
}
