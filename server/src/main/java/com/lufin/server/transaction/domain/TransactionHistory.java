package com.lufin.server.transaction.domain;

import java.time.LocalDateTime;

import com.lufin.server.account.domain.Account;
import com.lufin.server.common.constants.HistoryStatus;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "transaction_histories")
public class TransactionHistory {

	@Id
	@GeneratedValue
	@Column(name = "transaction_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_account_id", nullable = true)
	private Account fromAccount;

	@Column(name = "to_account_number", nullable = false)
	private String toAccountNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member executor;

	@Column(name = "transaction_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	@Column(name = "transaction_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private TransactionSourceType sourceType;

	@Column(name = "amount", nullable = false)
	private Integer amount;

	@Column(name = "balance_after", nullable = false)
	private Integer balanceAfter;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private HistoryStatus status;

	@Column(name = "executor_role", nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberRole executorRole;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private TransactionHistory(
		Account fromAccount,
		String toAccountNumber,
		Member executor,
		int amount,
		int balanceAfter,
		TransactionType type,
		HistoryStatus status,
		String description,
		TransactionSourceType sourceType
	) {
		this.fromAccount = fromAccount;
		this.toAccountNumber = toAccountNumber;
		this.executor = executor;
		this.amount = amount;
		this.balanceAfter = balanceAfter;
		this.transactionType = type;
		this.status = status;
		this.description = description;
		this.sourceType = sourceType;
		this.executorRole = executor.getMemberRole();
	}

	public static TransactionHistory create(
		Account fromAccount,
		String toAccountNumber,
		Member executor,
		int amount,
		int balanceAfter,
		TransactionType type,
		HistoryStatus status,
		String description,
		TransactionSourceType sourceType
	) {
		return new TransactionHistory(
			fromAccount,
			toAccountNumber,
			executor,
			amount,
			balanceAfter,
			type,
			status,
			description,
			sourceType
		);
	}

	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
