package com.lufin.server.credit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.credit.domain.CreditScore;

public interface CreditScoreRepository extends JpaRepository<CreditScore, Integer> {

	Optional<CreditScore> findByMemberClassroom(MemberClassroom memberClassroom);
}
