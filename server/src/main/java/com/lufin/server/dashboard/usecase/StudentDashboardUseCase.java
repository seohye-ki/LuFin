package com.lufin.server.dashboard.usecase;

import static com.lufin.server.dashboard.util.AssetStatUtils.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lufin.server.account.service.AccountService;
import com.lufin.server.credit.dto.CreditHistoryDto;
import com.lufin.server.credit.service.CreditService;
import com.lufin.server.dashboard.dto.AssetDto;
import com.lufin.server.dashboard.dto.StudentDashboardDto;
import com.lufin.server.item.dto.ItemDashboardDto;
import com.lufin.server.item.service.ItemPurchaseService;
import com.lufin.server.loan.service.LoanDashboardService;
import com.lufin.server.member.dto.RankingDto;
import com.lufin.server.member.service.RankingService;
import com.lufin.server.mission.dto.MyMissionDto;
import com.lufin.server.mission.service.MyMissionService;
import com.lufin.server.stock.service.StockService;
import com.lufin.server.transaction.service.TransactionHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentDashboardUseCase {

	private final RankingService rankingService;
	private final CreditService creditService;
	private final AccountService accountService;
	private final StockService stockService;
	private final LoanDashboardService loanService;
	private final ItemPurchaseService itemService;
	private final MyMissionService myMissionService;
	private final TransactionHistoryService transactionHistoryService;

	public StudentDashboardDto getDashboard(int studentId, int classId) {

		log.info("[학생 대시보드 요청] studentId={}, classId={}", studentId, classId);

		// 랭킹
		List<RankingDto> rankings = rankingService.getAssetRanking(classId);
		log.debug(" - 랭킹 수: {}", rankings.size());

		// 신용 점수 및 등급
		int creditScore = creditService.getScore(studentId);
		String creditGrade = creditService.getGrade(studentId);
		List<CreditHistoryDto> creditHistories = creditService.getGradeChangeHistory(studentId);
		log.debug(" - 신용 점수: {}, 등급: {}, 이력 수: {}", creditScore, creditGrade, creditHistories.size());

		// 자산 구성
		int cash = accountService.getCashBalance(studentId, classId);
		int stock = stockService.getTotalValuation(studentId);
		int loan = loanService.getLoanPrincipal(studentId, classId);
		int totalAsset = cash + stock - loan;
		log.debug(" - 자산: 현금={}, 주식={}, 대출={}, 총합={}", cash, stock, loan, totalAsset);

		// 소비 통계
		int weeklyConsumption = transactionHistoryService.getTotalConsumptionThisWeek(studentId);
		int consumptionLastWeek =
			weeklyConsumption - transactionHistoryService.getConsumptionChangeSinceLastWeek(studentId);
		AssetDto consumptionStat = buildAssetDto("소비", weeklyConsumption, consumptionLastWeek);

		// 투자 통계 (오늘 vs 어제 또는 금요일)
		LocalDate today = LocalDate.now();
		LocalDate compareDate = (today.getDayOfWeek() == DayOfWeek.MONDAY)
			? today.minusDays(3)
			: today.minusDays(1);

		int investmentToday = stockService.getInvestmentAmountOnDate(studentId, today);
		int investmentCompare = stockService.getInvestmentAmountOnDate(studentId, compareDate);
		AssetDto investmentStat = buildAssetDto("투자", investmentToday, investmentCompare);

		// 아이템 보유 현황
		List<ItemDashboardDto> items = itemService.getMyItems(studentId, classId);
		log.debug(" - 아이템 수: {}", items.size());

		// 미션 상태
		List<MyMissionDto> ongoingMissions = myMissionService.getMyMissions(classId, studentId);
		int totalCompletedMissions = myMissionService.getCompletedCount(studentId);
		int totalWage = myMissionService.getTotalWage(studentId);
		log.debug(" - 미션: 진행중={}, 완료={}, 누적 보상={}", ongoingMissions.size(), totalCompletedMissions, totalWage);

		log.info("[학생 대시보드 구성 완료] studentId={}", studentId);

		return StudentDashboardDto.builder()
			.rankings(rankings)
			.creditScore(creditScore)
			.creditGrade(creditGrade)
			.creditHistories(creditHistories)
			.cash(cash)
			.stock(stock)
			.loan(loan)
			.totalAsset(totalAsset)
			.investmentStat(investmentStat)
			.consumptionStat(consumptionStat)
			.items(items)
			.ongoingMissions(ongoingMissions)
			.totalCompletedMissions(totalCompletedMissions)
			.totalWage(totalWage)
			.build();
	}
}
