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

	/**
	 * 학생 보유 현금 합계 = 예금 - 대출
	 */
	public long getTotalStudentCash(int classId, LocalDate date) {
		List<MemberClassroom> students = memberClassroomRepository.findStudentsByClassId(classId);
		long total = 0;

		for (MemberClassroom mc : students) {
			long deposit = accountService.getTotalClassDeposit(classId, date);
			long loan = loanDashboardService.getTotalClassLoanPrincipal(classId);
			total += (deposit - loan);
		}

		return total;
	}

	/**
	 * 학생 주식 평가액 합계 (보유 기준)
	 */
	public long getTotalStudentStock(int classId, LocalDate date) {
		List<MemberClassroom> students = memberClassroomRepository.findStudentsByClassId(classId);
		long total = 0;

		for (MemberClassroom mc : students) {
			int memberId = mc.getMember().getId();
			long valuation = stockService.getTotalValuationOnDate(memberId, classId, date);
			total += valuation;
		}

		return total;
	}

	/**
	 * 학생 대출 원금 합계
	 */
	public long getTotalStudentLoan(int classId, LocalDate date) {
		List<MemberClassroom> students = memberClassroomRepository.findStudentsByClassId(classId);
		long total = 0;

		for (MemberClassroom mc : students) {
			long loan = loanDashboardService.getTotalClassLoanPrincipal(classId);
			total += loan;
		}

		return total;
	}
}
