package com.lufin.server.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lufin.server.account.domain.Account;
import com.lufin.server.member.domain.MemberRole;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	// 계좌번호로 계좌 조회
	Optional<Account> findByAccountNumber(String accountNumber);

	// 특정 멤버 ID의 모든 계좌 조회
	List<Account> findByMemberId(Integer memberId);

	// TODO: class 계좌 찾기 추가

	// 교사 역할을 가진 멤버의 계좌들 조회
	@Query("SELECT a FROM Account a WHERE a.member.memberRole = 'TEACHER'")
	List<Account> findAllTeacherAccounts();

	Optional<Account> findFirstByMemberMemberRole(MemberRole role);
}
