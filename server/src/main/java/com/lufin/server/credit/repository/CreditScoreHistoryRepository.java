package com.lufin.server.credit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lufin.server.credit.domain.CreditScoreHistory;

public interface CreditScoreHistoryRepository extends JpaRepository<CreditScoreHistory, Integer> {

	@Query("""
			SELECT h FROM CreditScoreHistory h
			WHERE h.memberClassroom.member.id = :memberId
			  AND h.memberClassroom.classroom.id = :classId
			ORDER BY h.createdAt DESC
		""")
	List<CreditScoreHistory> findTop10ByMemberIdAndClassIdOrderByCreatedAtDesc(@Param("memberId") int memberId,
		@Param("classId") int classId);
}
