package com.lufin.server.account.domain;

import java.time.LocalDateTime;

import com.lufin.server.common.constants.HistoryStatus;
import com.lufin.server.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Account fromAccount;

	private String toAccountNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member executor;

	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	@Enumerated(EnumType.STRING)
	private TransactionSourceType sourceType;

	private Integer amount;

	private Integer balanceAfter;

	@Enumerated(EnumType.STRING)
	private HistoryStatus status;

	private String description;

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
