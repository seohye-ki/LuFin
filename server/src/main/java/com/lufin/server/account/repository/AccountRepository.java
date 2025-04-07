package com.lufin.server.account.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.domain.AccountType;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	// 계좌번호로 계좌 조회
	Optional<Account> findByAccountNumber(String accountNumber);

	// 특정 멤버 ID의 모든 계좌 조회
	List<Account> findByMemberId(Integer memberId);

	// 특정 클래스 계좌 조회
	Optional<Account> findByClassroomId(Integer classroomId);

	// 현재 클래스의 특정 멤버 계좌 조회 (비관적 락 사용)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Account a WHERE a.closedAt IS NULL AND a.member.id = :memberId")
	Optional<Account> findOpenAccountByMemberIdWithPessimisticLock(
		@Param("memberId") Integer memberId);

	// 계좌 번호 유무 조회
	boolean existsByAccountNumber(String newAccountNumber);

	// 특정 멤버 ID의 활성 계좌 (closedAt == null) 조회
	Optional<Account> findByMemberIdAndClosedAtIsNull(Integer memberId);

	Optional<Account> findByMemberIdAndClassroomIdAndType(int memberId, int classId, AccountType type);


	// 1년 뒤 해지
	@Query("SELECT a FROM Account a WHERE a.type = 'CLASSROOM' AND a.createdAt < :limit AND a.closedAt IS NULL")
	List<Account> findClassAccountsBefore(@Param("limit") LocalDateTime limit);

}
