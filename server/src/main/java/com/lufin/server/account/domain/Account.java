package com.lufin.server.account.domain;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.time.LocalDateTime;

import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Getter
@Entity
@Table(name = "account", uniqueConstraints = @UniqueConstraint(columnNames = "accountNumber"))
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true, length = 30)
	private String accountNumber;

	@Column(nullable = false)
	private Integer balance;

	// 학생 (혹은 교사 개인 계좌)의 경우만 사용
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	// TODO: 클래스 계좌일 경우 추가

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime closedAt;

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

	public boolean isClosed() {
		return closedAt != null;
	}
}
