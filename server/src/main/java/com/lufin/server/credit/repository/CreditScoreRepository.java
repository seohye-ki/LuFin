package com.lufin.server.credit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.credit.domain.CreditScore;

public interface CreditScoreRepository extends JpaRepository<CreditScore, Integer> {

	Optional<CreditScore> findByMemberId(int memberId);
}
