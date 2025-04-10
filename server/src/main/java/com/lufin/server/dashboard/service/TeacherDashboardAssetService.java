package com.lufin.server.dashboard.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lufin.server.account.service.AccountService;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.loan.service.LoanDashboardService;
import com.lufin.server.stock.service.StockService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherDashboardAssetService {

	private final MemberClassroomRepository memberClassroomRepository;
	private final AccountService accountService;
	private final StockService stockService;
	private final LoanDashboardService loanDashboardService;

	// 학생 보유 현금 합계 = 예금 - 대출
	public long getTotalStudentCash(int classId, LocalDate date) {
		List<MemberClassroom> students = memberClassroomRepository.findStudentsByClassId(classId);
		long total = 0;

		// 각 학생의 현금 자산 계산
		for (MemberClassroom mc : students) {
			int memberId = mc.getMember().getId();

			// 학생 예금과 대출을 각각 계산
			long deposit = accountService.getCashBalance(memberId, classId) - loanDashboardService.getLoanPrincipal(memberId, classId);
			total += deposit; // 각 학생의 현금 합산
		}

		return total;
	}

	// 학생 주식 평가액 합계 (보유 기준)
	public long getTotalStudentStock(int classId, LocalDate date) {
		List<MemberClassroom> students = memberClassroomRepository.findStudentsByClassId(classId);
		long total = 0;

		// 각 학생의 보유 주식 평가액 계산
		for (MemberClassroom mc : students) {
			int memberId = mc.getMember().getId();
			long valuation = stockService.getTotalValuationOnDate(memberId, classId, date);
			total += valuation; // 각 학생의 주식 평가액 합산
		}

		return total;
	}

	// 학생 대출 원금 합계
	public long getTotalStudentLoan(int classId, LocalDate date) {
		List<MemberClassroom> students = memberClassroomRepository.findStudentsByClassId(classId);
		long total = 0;

		// 각 학생의 대출 원금 합산
		for (MemberClassroom mc : students) {
			int memberId = mc.getMember().getId();
			long loan = loanDashboardService.getLoanPrincipal(memberId, classId); // 학생별 대출 원금
			total += loan; // 학생 대출 원금 합산
		}

		return total;
	}
}
