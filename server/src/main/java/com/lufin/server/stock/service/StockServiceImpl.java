package com.lufin.server.stock.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.lufin.server.stock.domain.StockTransactionHistory;
import com.lufin.server.stock.repository.StockRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

	private final StockRepository stockRepository;

	@Override
	public int getTotalValuation(int memberId) {
		log.info("[보유한 주식의 총 평가 금액 조회] memberId = {}", memberId);
		return stockRepository.findAllByMemberId(memberId).stream()
			.mapToInt(StockTransactionHistory::getTotalPrice)
			.sum();
	}

	@Override
	public int getInvestmentAmountOnDate(int memberId, LocalDate date) {
		log.info("[특정 날짜의 투자 금액 총합] memberId = {}, date = {}", memberId, date);
		return stockRepository.findAllByMemberIdAndCreatedAtBetween(
				memberId,
				date.atStartOfDay(),
				date.plusDays(1).atStartOfDay()
			).stream()
			.mapToInt(StockTransactionHistory::getTotalPrice)
			.sum();
	}
}
