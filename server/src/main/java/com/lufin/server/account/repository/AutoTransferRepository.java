package com.lufin.server.account.repository;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lufin.server.account.domain.AutoTransfer;

public interface AutoTransferRepository extends JpaRepository<AutoTransfer, Integer> {

	// 현재 활성화된 자동이체 전체 조회
	List<AutoTransfer> findByActiveTrue();

	// 특정 요일에 실행되며 활성화된 자동이체 조회
	List<AutoTransfer> findByDayOfWeekAndActiveTrue(DayOfWeek dayOfWeek);

	// 특정 출금 계좌에서 실행되는 활성화된 자동이체 조회
	List<AutoTransfer> findByFrom_IdAndActiveTrue(Integer fromAccountId);

	// 종료일이 오늘 이전인 자동이체 중 아직 비활성화되지 않은 항목 조회
	@Query("""
			SELECT a FROM AutoTransfer a
			WHERE a.endDate IS NOT NULL AND a.endDate < CURRENT_DATE
			AND a.active = true
		""")
	List<AutoTransfer> findExpiredTransfers(); // 비활성화 처리 필요
}
