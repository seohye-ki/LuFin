package com.lufin.server.dashboard.usecase;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lufin.server.account.service.AccountService;
import com.lufin.server.credit.dto.CreditHistoryDto;
import com.lufin.server.credit.service.CreditService;
import com.lufin.server.dashboard.dto.StudentDashboardDto;
import com.lufin.server.item.dto.ItemDashboardDto;
import com.lufin.server.item.service.ItemPurchaseService;
import com.lufin.server.loan.service.LoanService;
import com.lufin.server.member.dto.RankingDto;
import com.lufin.server.member.service.RankingService;
import com.lufin.server.mission.dto.MyMissionDto;
import com.lufin.server.mission.service.MyMissionService;
import com.lufin.server.stock.service.StockService;
import com.lufin.server.transaction.service.TransactionHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentDashboardUseCase {

	private final RankingService rankingService;
	private final CreditService creditService;
	private final AccountService accountService;
	private final StockService stockService;
	private final LoanService loanService;
	private final ItemPurchaseService itemService;
	private final MyMissionService myMissionService;
	private final TransactionHistoryService transactionHistoryService;

	public StudentDashboardDto getDashboard(int studentId, int classId) {

		List<RankingDto> rankings = rankingService.getAssetRanking();

		int creditScore = creditService.getScore(studentId);
		String creditGrade = creditService.getGrade(studentId);
		List<CreditHistoryDto> creditHistories = creditService.getGradeChangeHistory(studentId);

		int cash = accountService.getCashBalance(studentId);
		int stock = stockService.getTotalValuation(studentId);
		int loan = loanService.getLoanPrincipal(studentId, classId);
		int totalAsset = cash + stock;

		int weeklyConsumption = transactionHistoryService.getTotalConsumptionThisWeek(studentId);
		int weeklyConsumptionChange = transactionHistoryService.getConsumptionChangeSinceLastWeek(studentId);

		LocalDate today = LocalDate.now();
		LocalDate compareDate = (today.getDayOfWeek() == DayOfWeek.MONDAY)
			? today.minusDays(3)
			: today.minusDays(1);
		int investmentToday = stockService.getInvestmentAmountOnDate(studentId, today);
		int investmentCompare = stockService.getInvestmentAmountOnDate(studentId, compareDate);
		int investmentChange = investmentToday - investmentCompare;

		List<ItemDashboardDto> items = itemService.getMyItems(studentId, classId);

		List<MyMissionDto> ongoingMissions = myMissionService.getMyMissions(classId, studentId);
		int totalCompletedMissions = myMissionService.getCompletedCount(studentId);
		int totalReward = myMissionService.getTotalWage(studentId);

		return StudentDashboardDto.builder()
			.rankings(rankings)
			.creditScore(creditScore)
			.creditGrade(creditGrade)
			.creditHistories(creditHistories)
			.cash(cash)
			.stock(stock)
			.loan(loan)
			.totalAsset(totalAsset)
			.weeklyConsumption(weeklyConsumption)
			.weeklyConsumptionChange(weeklyConsumptionChange)
			.investmentAmount(investmentToday)
			.investmentChange(investmentChange)
			.loanPrincipal(loan)
			.items(items)
			.ongoingMissions(ongoingMissions)
			.totalCompletedMissions(totalCompletedMissions)
			.totalReward(totalReward)
			.build();
	}
}
