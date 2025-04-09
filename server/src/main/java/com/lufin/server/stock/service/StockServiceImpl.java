package com.lufin.server.stock.service;

import java.time.LocalDate;
import java.util.List;

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
	public int getTotalValuation(int memberId, int classId) {
		log.info("[보유한 주식의 총 평가 금액 조회] memberId = {}", memberId);
		// 보유 중인 주식 리스트 조회 후 평가 금액 합산
		return stockRepository.findAllByMemberIdAndClassroomId(memberId, classId).stream()
			.mapToInt(StockTransactionHistory::getTotalPrice)
			.sum();
	}

	@Override
	public int getTotalValuationOnDate(int memberId, int classId, LocalDate date) {
		log.info("[{} 기준 보유 주식 평가액 조회] memberId={}, classId={}", date, memberId, classId);

		// 해당 시점까지의 모든 매수/매도 내역 조회 후 평가
		List<StockTransactionHistory> stocks = stockRepository
			.findAllByMemberIdAndClassroomIdAndCreatedAtLessThanEqual(
				memberId,
				classId,
				date.atTime(23, 59, 59)
			);

		// 주식 평가 금액 계산
		return stocks.stream()
			.mapToInt(StockTransactionHistory::getTotalPrice)
			.sum();
	}
}
