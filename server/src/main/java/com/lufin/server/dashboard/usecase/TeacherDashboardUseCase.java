package com.lufin.server.dashboard.usecase;

import static com.lufin.server.dashboard.util.AssetStatUtils.*;
import static com.lufin.server.item.domain.ItemPurchaseStatus.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lufin.server.account.service.AccountService;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.credit.service.CreditService;
import com.lufin.server.dashboard.dto.AssetDto;
import com.lufin.server.dashboard.dto.TeacherDashboardDto;
import com.lufin.server.dashboard.dto.TeacherDashboardDto.StatisticsDto;
import com.lufin.server.dashboard.dto.TeacherDashboardDto.StudentItemDto;
import com.lufin.server.item.repository.ItemPurchaseRepository;
import com.lufin.server.loan.domain.LoanApplicationStatus;
import com.lufin.server.loan.dto.LoanApplicationListDto;
import com.lufin.server.loan.service.LoanDashboardService;
import com.lufin.server.loan.service.LoanService;
import com.lufin.server.member.domain.Member;
import com.lufin.server.mission.dto.MyMissionDto;
import com.lufin.server.mission.service.MyMissionService;
import com.lufin.server.stock.service.StockService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherDashboardUseCase {

	private final MemberClassroomRepository memberClassroomRepository;
	private final AccountService accountService;
	private final StockService stockService;
	private final LoanDashboardService loanDashboardService;
	private final LoanService loanService;
	private final CreditService creditService;
	private final MyMissionService myMissionService;
	private final ItemPurchaseRepository itemService;

	public TeacherDashboardDto getDashboard(int classId) {

		// 해당 클래스에 속한 모든 구성원 조회
		List<MemberClassroom> memberClassrooms = memberClassroomRepository.findStudentsByClassId(classId);

		// 자산 통계
		// - 예금, 대출: 1주일 전과 현재 잔액 비교
		// - 투자: 어제와 오늘 투자금액 비교 (거래 기준)
		AssetDto deposit = buildAssetDto(
			"예금",
			accountService.getTotalClassDeposit(classId),
			accountService.getTotalClassDeposit(classId, LocalDate.now().minusWeeks(1))
		);

		AssetDto investment = buildAssetDto(
			"투자",
			stockService.getTotalClassInvestment(classId, LocalDate.now()),
			stockService.getTotalClassInvestment(classId, LocalDate.now().minusDays(1))
		);

		AssetDto loan = buildAssetDto(
			"대출",
			loanDashboardService.getTotalClassLoanPrincipal(classId),
			loanDashboardService.getTotalClassLoanPrincipal(classId, LocalDate.now().minusWeeks(1))
		);

		StatisticsDto statistics = StatisticsDto.builder()
			.deposit(deposit)
			.investment(investment)
			.loan(loan)
			.build();

		// 각 학생의 개인 현황 정리
		List<StudentItemDto> students = memberClassrooms.stream().map(mc -> {
			Member student = mc.getMember();
			int studentId = student.getId();

			// 학생 자산 정보
			int cash = accountService.getCashBalance(studentId, classId);
			int stock = stockService.getTotalValuation(studentId);
			int loanAmount = loanDashboardService.getLoanPrincipal(studentId, classId);

			// 신용등급
			String creditGrade = creditService.getGrade(studentId);

			// 미션 상태 판단
			List<MyMissionDto> missions = myMissionService.getMyMissions(classId, studentId);
			String missionStatus = getMissionStatus(missions);

			// 대출 상태 판단
			List<LoanApplicationListDto> loans = loanService.getLoanApplications(student, classId);
			String loanStatus = getLoanStatus(loans);

			// 아이템 보유 수량
			int items = itemService.findInventory(studentId, BUY, classId).size();

			// 학생 대시보드용 DTO 생성
			return StudentItemDto.builder()
				.id(studentId)
				.name(student.getName())
				.cash(cash)
				.investment(stock)
				.loan(loanAmount)
				.creditGrade(creditGrade)
				.missionStatus(missionStatus)
				.loanStatus(loanStatus)
				.items(items)
				.build();
		}).toList();

		// 최종 대시보드 응답 조립
		return TeacherDashboardDto.builder()
			.statistics(statistics)
			.students(students)
			.build();
	}

	private String getMissionStatus(List<MyMissionDto> missions) {
		boolean hasOngoing = missions.stream().anyMatch(m -> m.status().name().equals("IN_PROGRESS"));
		boolean hasCompleted = missions.stream().allMatch(m -> m.status().name().equals("CLOSED"));
		if (hasOngoing) {
			return "수행 중";
		}
		if (hasCompleted) {
			return "수행 완료";
		}
		return "검토 필요";
	}

	private String getLoanStatus(List<LoanApplicationListDto> loans) {
		if (loans == null || loans.isEmpty()) {
			return "없음";
		}

		boolean hasPending = loans.stream().anyMatch(l -> l.status() == LoanApplicationStatus.PENDING);
		if (hasPending) {
			return "검토 필요";
		}

		boolean allRejected = loans.stream().allMatch(l -> l.status() == LoanApplicationStatus.REJECTED);
		if (allRejected) {
			return "거절";
		}

		// 그 외
		return "승인";
	}
}
















