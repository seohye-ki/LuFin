package com.lufin.server.stock.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.stock.domain.StockTransactionHistory;
import com.lufin.server.stock.repository.StockRepository;
import com.lufin.server.transaction.domain.TransactionHistory;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.repository.TransactionHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

	private final StockRepository stockRepository;
	private final MemberClassroomRepository memberClassroomRepository;
	private final TransactionHistoryRepository transactionHistoryRepository;

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

	@Override
	public long getTotalClassInvestment(int classId, LocalDate date) {
		log.info("[투자 일일 합계 조회] classId={}, 기준일={}", classId, date);

		List<MemberClassroom> students = memberClassroomRepository.findStudentsByClassId(classId);

		LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
		LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59

		long total = 0;

		for (MemberClassroom mc : students) {
			int memberId = mc.getMember().getId();

			// 기준일 하루 동안의 투자 구매 거래 조회
			List<TransactionHistory> dailyInvestments = transactionHistoryRepository
				.findAllByExecutorIdAndSourceTypeAndCreatedAtBetween(
					memberId,
					TransactionSourceType.INVESTMENT_BUY,
					startOfDay,
					endOfDay
				);

			int dailyTotal = dailyInvestments.stream()
				.mapToInt(TransactionHistory::getAmount)
				.sum();

			log.debug(" - memberId={}, 기준일 투자합계={}", memberId, dailyTotal);
			total += dailyTotal;
		}

		log.debug("투자 일일 총합 완료] classId={}, 기준일={}, totalDailyInvestment={}", classId, date, total);
		return total;
	}

}
