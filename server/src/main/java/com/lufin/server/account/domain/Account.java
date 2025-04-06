package com.lufin.server.account.domain;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.time.LocalDateTime;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "accounts", uniqueConstraints = @UniqueConstraint(columnNames = "accountNumber"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classroom_id", unique = true)
	private Classroom classroom;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_type", nullable = false)
	private AccountType type; // 예: DEPOSIT, LOAN, CLASSROOM

	@Column(nullable = false, unique = true, length = 15)
	private String accountNumber;

	@Column(nullable = false)
	private Integer balance;

	// 학생 (혹은 교사 개인 계좌)의 경우만 사용
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime closedAt;

	private Account(String accountNumber, AccountType type, Member member) {
		this.accountNumber = accountNumber;
		this.member = member;
		this.type = type;
		onCreate();
	}

	private Account(String accountNumber, AccountType type, Classroom classroom) {
		this.accountNumber = accountNumber;
		this.classroom = classroom;
		this.type = type;
		onCreate();
	}

	public static Account create(String accountNumber, Member member) {
		return new Account(accountNumber, AccountType.DEPOSIT, member);
	}

	public static Account createClassAccount(String accountNumber, Classroom classroom) {
		return new Account(accountNumber, AccountType.CLASSROOM, classroom);
	}

	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
		if (this.balance == null) {
			this.balance = 0;
		}
	}

	public void deposit(int amount) {
		if (amount <= 0) {
			throw new BusinessException(INVALID_DEPOSIT_AMOUNT);
		}
		this.balance += amount;
	}

	public void withdraw(int amount) {
		if (amount <= 0) {
			throw new BusinessException(INVALID_WITHDRAWAL_AMOUNT);
		}
		if (this.balance < amount) {
			throw new BusinessException(INSUFFICIENT_BALANCE);
		}
		this.balance -= amount;
	}

	public void forceWithdraw(int amount) {
		if (amount <= 0) {
			throw new BusinessException(INVALID_WITHDRAWAL_AMOUNT);
		}
		this.balance -= amount;
	}

	public boolean isClosed() {
		return closedAt != null;
	}

	public void close() {
		if (isClosed()) {
			throw new BusinessException(ALREADY_CLOSED_ACCOUNT);
		}
		this.closedAt = LocalDateTime.now();
	}

}
