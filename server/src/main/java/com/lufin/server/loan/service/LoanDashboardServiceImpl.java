package com.lufin.server.loan.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.loan.domain.LoanApplication;
import com.lufin.server.loan.repository.LoanApplicationRepository;
import com.lufin.server.transaction.domain.TransactionHistory;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.repository.TransactionHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanDashboardServiceImpl implements LoanDashboardService {

	private final LoanApplicationRepository loanApplicationRepository;
	private final TransactionHistoryRepository transactionHistoryRepository;
	private final MemberClassroomRepository memberClassroomRepository;

	@Override
	public int getLoanPrincipal(int memberId, int classroomId) {
		log.info("[대출 원금 조회] memberId: {}, classroomId: {}", memberId, classroomId);
		return loanApplicationRepository.findMyLoanApplication(memberId, classroomId)
			.map(LoanApplication::getRequiredAmount)
			.orElse(0);
	}

	@Override
	public long getTotalClassLoanPrincipal(int classId) {
		log.info("[대출 총원금 조회] classId={}", classId);

		List<MemberClassroom> students = memberClassroomRepository.findStudentsByClassId(classId);

		// 각 학생이 해당 반에서 받은 대출의 원금 합산
		long total = students.stream()
			.mapToLong(mc -> getLoanPrincipal(mc.getMember().getId(), classId))
			.sum();

		log.debug("[대출 총액 결과] classId={}, total={}", classId, total);
		return total;
	}

	@Override
	public long getTotalClassLoanPrincipal(int classId, LocalDate date) {
		log.info("[기준일 대출액 조회] classId={}, 기준일={}", classId, date);

		List<MemberClassroom> students = memberClassroomRepository.findStudentsByClassId(classId);

		long total = 0;

		for (MemberClassroom mc : students) {
			int memberId = mc.getMember().getId();

			// 해당 학생의 '기준일 이전' 대출 집행 거래 중 가장 최근 거래 기록 조회
			Optional<TransactionHistory> latest = transactionHistoryRepository
				.findTopByExecutor_IdAndSourceTypeAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
					memberId,
					TransactionSourceType.LOAN_DISBURSEMENT,
					date.atTime(23, 59, 59)
				);

			// 해당 시점 대출 잔액 합산
			int balance = latest.map(TransactionHistory::getBalanceAfter).orElse(0);
			log.debug(" - memberId={}, 기준일 이전 대출 원금={}", memberId, balance);
			total += balance;
		}

		log.debug("[기준일 대출 합산 완료] classId={}, 기준일={}, total={}", classId, date, total);
		return total;
	}
}
