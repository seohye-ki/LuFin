package com.lufin.server.transaction.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	private static final EnumSet<TransactionSourceType> CONSUMPTION_TYPES = EnumSet.of(
		TransactionSourceType.WITHDRAWAL,
		TransactionSourceType.ITEM_PURCHASE
	);

	@Override
	public int getTotalConsumptionThisWeek(int memberId) {
		log.info("[이번 주의 소비 총액 조회] memberId: {}", memberId);
		LocalDate today = LocalDate.now();
		LocalDate monday = today.with(DayOfWeek.MONDAY);
		LocalDateTime start = monday.atStartOfDay();
		LocalDateTime end = today.plusDays(1).atStartOfDay();

		return sumConsumption(memberId, start, end);
	}

	@Override
	public int getConsumptionChangeSinceLastWeek(int memberId) {
		log.info("[지난주 대비 소비 증감량 조회] memberId: {}", memberId);
		LocalDate today = LocalDate.now();
		LocalDate lastWeekStart = today.with(DayOfWeek.MONDAY).minusWeeks(1);
		LocalDate lastWeekEnd = lastWeekStart.plusDays(6);

		int lastWeek = sumConsumption(
			memberId,
			lastWeekStart.atStartOfDay(),
			lastWeekEnd.plusDays(1).atStartOfDay()
		);
		int thisWeek = getTotalConsumptionThisWeek(memberId);
		return thisWeek - lastWeek;
	}

	private int sumConsumption(int memberId, LocalDateTime from, LocalDateTime to) {
		List<TransactionHistory> records = historyRepository.findAllByExecutorIdAndCreatedAtBetween(
			memberId, from, to
		);
		return records.stream()
			.filter(tx -> CONSUMPTION_TYPES.contains(tx.getSourceType()))
			.mapToInt(TransactionHistory::getAmount)
			.sum();
	}
}
