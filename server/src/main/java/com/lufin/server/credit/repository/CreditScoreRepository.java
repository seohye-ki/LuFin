package com.lufin.server.credit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lufin.server.credit.domain.CreditScore;

public interface CreditScoreRepository extends JpaRepository<CreditScore, Integer> {

	@Query("""
			SELECT cs FROM CreditScore cs
			JOIN MemberClassroom mc ON cs.member = mc.member
			WHERE cs.member.id = :memberId AND mc.classroom.id = :classId
		""")
	Optional<CreditScore> findByMemberIdAndClassId(int memberId, int classId);
}
