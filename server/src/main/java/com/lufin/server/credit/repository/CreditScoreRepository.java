package com.lufin.server.credit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lufin.server.credit.domain.CreditScore;

public interface CreditScoreRepository extends JpaRepository<CreditScore, Integer> {

	@Query("""
			SELECT cs FROM CreditScore cs
			WHERE cs.memberClassroom.member.id = :memberId
			AND cs.memberClassroom.classroom.id = :classId
		""")
	Optional<CreditScore> findByMemberIdAndClassId(@Param("memberId") int memberId, @Param("classId") int classId);


	Optional<CreditScore> findByMemberId(int memberId);
}
