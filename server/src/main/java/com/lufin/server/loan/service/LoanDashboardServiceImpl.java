package com.lufin.server.loan.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.transaction.domain.TransactionHistory;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.repository.TransactionHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanDashboardServiceImpl implements LoanDashboardService {

	private final TransactionHistoryRepository transactionHistoryRepository;
	private final MemberClassroomRepository memberClassroomRepository;

	@Override
	public int getLoanPrincipal(int memberId, int classroomId) {
		log.info("[대출 원금 조회] memberId: {}, classroomId: {}", memberId, classroomId);

		// 가장 최근 대출 실행 원금 반환
		return transactionHistoryRepository
			.findTopByExecutor_IdAndSourceTypeAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
				memberId,
				TransactionSourceType.LOAN_DISBURSEMENT,
				LocalDateTime.now()
			)
			.map(TransactionHistory::getAmount)
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
}
