package com.lufin.server.credit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.credit.domain.CreditScore;
import com.lufin.server.member.domain.Member;

public interface CreditScoreRepository extends JpaRepository<CreditScore, Integer> {
	Optional<CreditScore> findByMember(Member member);
}
