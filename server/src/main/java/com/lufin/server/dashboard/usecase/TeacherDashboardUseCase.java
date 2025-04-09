package com.lufin.server.dashboard.usecase;

import static com.lufin.server.dashboard.util.AssetStatUtils.*;
import static com.lufin.server.item.domain.ItemPurchaseStatus.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lufin.server.account.service.AccountService;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.credit.service.CreditService;
import com.lufin.server.dashboard.dto.AssetDto;
import com.lufin.server.dashboard.dto.TeacherDashboardDto;
import com.lufin.server.dashboard.dto.TeacherDashboardDto.StatisticsDto;
import com.lufin.server.dashboard.dto.TeacherDashboardDto.StudentItemDto;
import com.lufin.server.dashboard.service.TeacherDashboardAssetService;
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
	private final TeacherDashboardAssetService teacherDashboardAssetService;

	public TeacherDashboardDto getDashboard(int classId) {
		// 해당 클래스에 속한 모든 구성원 조회
		List<MemberClassroom> memberClassrooms = memberClassroomRepository.findStudentsByClassId(classId);

		if (memberClassrooms.isEmpty()) {
			// 학생이 없을 경우: 빈 통계 + 빈 학생 목록 반환
			StatisticsDto emptyStatistics = StatisticsDto.builder()
				.deposit(AssetDto.empty("학생 보유 현금"))
				.investment(AssetDto.empty("학생 보유 주식"))
				.loan(AssetDto.empty("학생 대출 원금"))
				.build();

			return TeacherDashboardDto.builder()
				.statistics(emptyStatistics)
				.students(Collections.emptyList())
				.build();
		}

		// 자산 통계
		LocalDate today = LocalDate.now();
		LocalDate lastWeek = today.minusWeeks(1);
		LocalDate compareStockDate = (today.getDayOfWeek() == DayOfWeek.MONDAY)
			? today.minusDays(3) // 월요일이면 금요일
			: today.minusDays(1); // 평일이면 어제

		long currentCash = teacherDashboardAssetService.getTotalStudentCash(classId, today);
		long prevCash = teacherDashboardAssetService.getTotalStudentCash(classId, lastWeek);

		long currentStock = teacherDashboardAssetService.getTotalStudentStock(classId, today);
		long prevStock = teacherDashboardAssetService.getTotalStudentStock(classId, compareStockDate);

		long currentLoan = teacherDashboardAssetService.getTotalStudentLoan(classId, today);
		long prevLoan = teacherDashboardAssetService.getTotalStudentLoan(classId, lastWeek);

		// 각 AssetDto 생성
		AssetDto deposit = buildAssetDto("학생 보유 현금", currentCash, prevCash, currentCash);
		AssetDto investment = buildAssetDto("학생 보유 주식", currentStock, prevStock, currentStock);
		AssetDto loan = buildAssetDto("학생 대출 원금", currentLoan, prevLoan, currentLoan);

		StatisticsDto statistics = StatisticsDto.builder()
			.deposit(deposit)
			.investment(investment)
			.loan(loan)
			.build();

		// 각 학생의 개인 현황 정리
		List<StudentItemDto> students = memberClassrooms.stream()
			.map(mc -> {
				Member student = mc.getMember();
				int studentId = student.getId();

				// 학생 자산 정보
				int stock = stockService.getTotalValuation(studentId, classId);
				int loanAmount = loanDashboardService.getLoanPrincipal(studentId, classId);
				int cash = accountService.getCashBalance(studentId, classId) - loanAmount;

				// 신용등급
				String creditGrade = creditService.getGrade(studentId, classId);

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
			})
			// 이름 기준으로 정렬 (ㄱ-ㅎ 순)
			.sorted(Comparator.comparing(StudentItemDto::getName))
			.collect(Collectors.toList());

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
