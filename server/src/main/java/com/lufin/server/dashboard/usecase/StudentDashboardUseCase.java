package com.lufin.server.dashboard.usecase;

import static com.lufin.server.common.constants.ErrorCode.*;
import static com.lufin.server.dashboard.util.AssetStatUtils.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lufin.server.account.service.AccountService;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.credit.dto.CreditHistoryDto;
import com.lufin.server.credit.service.CreditService;
import com.lufin.server.dashboard.dto.AssetDto;
import com.lufin.server.dashboard.dto.StudentDashboardDto;
import com.lufin.server.item.dto.ItemDashboardDto;
import com.lufin.server.item.service.ItemPurchaseService;
import com.lufin.server.loan.service.LoanDashboardService;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.dto.RankingDto;
import com.lufin.server.member.repository.MemberRepository;
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
	private final StockService stockService;
	private final AccountService accountService;
	private final LoanDashboardService loanService;
	private final ItemPurchaseService itemService;
	private final MyMissionService myMissionService;
	private final TransactionHistoryService transactionHistoryService;
	private final MemberRepository memberRepository;

	public StudentDashboardDto getDashboard(int studentId, int classId) {

		Member member = memberRepository.findById(studentId)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

		log.info("[학생 대시보드 요청] studentId={}, classId={}", studentId, classId);

		// 랭킹
		List<RankingDto> rankings = rankingService.getAssetRanking(classId);
		log.debug(" - 랭킹 수: {}", rankings.size());

		// 신용 점수 및 등급
		int creditScore = creditService.getScore(studentId, classId);
		String creditGrade = creditService.getGrade(studentId, classId);

		List<CreditHistoryDto> creditHistories = creditService.getGradeChangeHistory(studentId, classId);
		log.debug(" - 신용 점수: {}, 등급: {}, 이력 수: {}", creditScore, creditGrade, creditHistories.size());

		// 누적 소비 (클래스 입장 이후 전체)
		int accumulatedConsumption = transactionHistoryService.getTotalAccumulatedConsumption(studentId, classId);

		// 주간 소비 통계
		LocalDate today = LocalDate.now();
		LocalDate thisWeekStart = today.with(DayOfWeek.MONDAY);
		LocalDate lastWeekStart = thisWeekStart.minusWeeks(1);

		int thisWeek = transactionHistoryService.getTotalConsumptionBetween(studentId, classId, thisWeekStart,
			thisWeekStart.plusDays(6));
		int lastWeek = transactionHistoryService.getTotalConsumptionBetween(studentId, classId, lastWeekStart,
			lastWeekStart.plusDays(6));

		AssetDto consumptionStat = buildAssetDto("소비", thisWeek, lastWeek, accumulatedConsumption);

		/**
		 * 자산 구성
		 *
		 * cash: 예금 잔액 - 대출 원금
		 * - 대출을 받은 경우, 예금 잔액에 해당 금액이 포함되어 있으므로 반드시 차감해야 정확한 구성 가능
		 *
		 * stock: 현재 보유 중인 주식의 총 평가 금액
		 * - 현재 시세 기준, 보유 종목의 가치 합산
		 *
		 * loan: 대출 원금 (아직 갚지 않은 부채)
		 * - 자산 항목에는 포함하되, 시각적으로 부채 항목으로 따로 분리해서 보여줄 수 있음
		 *
		 * totalAsset: 전체 자산 구성 총합
		 * - cash + stock + loan
		 */

		int stock = stockService.getTotalValuation(studentId, classId);
		int loan = loanService.getLoanPrincipal(studentId, classId);
		int cash = accountService.getCashBalance(studentId, classId);
		cash -= loan;
		int totalAsset = cash + stock + loan;
		log.debug(" - 소비={}, 투자={}, 대출={}, 자산총합(현금+투자)={}", accumulatedConsumption, stock, loan, totalAsset);

		// 투자 통계 - 보유 주식 평가액 (오늘 vs 어제 또는 금요일)
		LocalDate compareDate = (today.getDayOfWeek() == DayOfWeek.MONDAY)
			? today.minusDays(3) // 월요일이면 금요일과 비교
			: today.minusDays(1); // 그 외는 전날

		int todayValuation = stockService.getTotalValuationOnDate(studentId, classId, today);
		int compareValuation = stockService.getTotalValuationOnDate(studentId, classId, compareDate);

		AssetDto investmentStat = buildAssetDto("투자", todayValuation, compareValuation, todayValuation);

		// 아이템 보유 현황
		List<ItemDashboardDto> items = itemService.getMyItems(studentId, classId);
		log.info(" - 아이템 수: {}", items.size());

		// 미션 상태
		List<MyMissionDto> ongoingMissions = myMissionService.hasOngoingMission(studentId, classId);
		int totalCompletedMissions = myMissionService.getCompletedCount(studentId, classId);
		int totalWage = myMissionService.getTotalWage(studentId, classId);
		log.info(" - 미션: 진행중={}, 완료={}, 누적 보상={}", ongoingMissions.size(), totalCompletedMissions, totalWage);

		log.info("[학생 대시보드 구성 완료] studentId={}", studentId);

		return StudentDashboardDto.builder()
			.myMemberId(studentId)
			.profileImage(member.getProfileImage())
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
