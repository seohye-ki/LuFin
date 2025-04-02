package com.lufin.server.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.lufin.server.account.domain.Account;
import com.lufin.server.member.domain.MemberRole;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	// 계좌번호로 계좌 조회
	Optional<Account> findByAccountNumber(String accountNumber);

	// 특정 멤버 ID의 모든 계좌 조회
	List<Account> findByMemberId(Integer memberId);

	// 특정 클래스 계좌 조회
	Optional<Account> findByClassroomId(Integer classroomId);

	// 특정 클래스의 특정 멤버 계좌 조회 (비관적 락 사용)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Account a WHERE a.classroom.id = :classroomId AND a.member.id = :memberId")
	Optional<Account> findByClassroomIdAndMemberIdWithPessimisticLock(
		@Param("classroomId") Integer classroomId,
		@Param("memberId") Integer memberId);

	// 교사 역할을 가진 멤버의 계좌들 조회
	@Query("SELECT a FROM Account a WHERE a.member.memberRole = 'TEACHER'")
	List<Account> findAllTeacherAccounts();

	Optional<Account> findFirstByMemberMemberRole(MemberRole role);

	// 계좌 번호 유무 조회
	boolean existsByAccountNumber(String newAccountNumber);
}
