package com.lufin.server.transaction.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.common.constants.HistoryStatus;
import com.lufin.server.member.domain.Member;
import com.lufin.server.transaction.domain.TransactionHistory;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.domain.TransactionType;
import com.lufin.server.transaction.repository.TransactionHistoryRepository;
import com.lufin.server.transaction.service.TransactionHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

	private final TransactionHistoryRepository historyRepository;

	@Override
	@Transactional
	public void record(
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
		TransactionHistory history = TransactionHistory.create(
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
		historyRepository.save(history);
	}
}
