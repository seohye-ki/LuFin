package com.lufin.server.transaction.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.common.constants.HistoryStatus;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.transaction.domain.TransactionHistory;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.domain.TransactionType;
import com.lufin.server.transaction.repository.TransactionHistoryRepository;
import com.lufin.server.transaction.service.TransactionHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

	private final TransactionHistoryRepository historyRepository;
	private final MemberClassroomRepository memberClassroomRepository;

	@Override
	@Transactional
	public void record(
		Account fromAccount,
		String toAccountNumber,
		Member executor,
		int amount,
		int balanceAfter,
		TransactionType type,
		HistoryStatus status,
		String description,
		TransactionSourceType sourceType
	) {
		TransactionHistory history = TransactionHistory.create(
			fromAccount,
			toAccountNumber,
			executor,
			amount,
			balanceAfter,
			type,
			status,
			description,
			sourceType
		);
		historyRepository.save(history);
	}

	/**
	 * 특정 기간 동안의 소비 금액 총합을 반환합니다.
	 * - 단, 해당 학생이 이 클래스에 입장한 이후의 거래만 계산됩니다.
	 * - 거래 유형: 출금(WITHDRAWAL), 아이템 구매(ITEM_PURCHASE)
	 */
	@Override
	public int getTotalConsumptionBetween(int memberId, int classId, LocalDate from, LocalDate to) {

		// 해당 멤버의 이 반 입장 일자 (joinDate)
		LocalDateTime joinDate = memberClassroomRepository
			.findByMemberIdAndClassroomId(memberId, classId)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND))
			.getJoinDate()
			.toLocalDate()
			.atStartOfDay();

		// 요청된 from ~ to 범위
		LocalDateTime fromTime = from.atStartOfDay();
		LocalDateTime toTime = to.atTime(23, 59, 59);

		// joinDate 이후의 거래만 유효하므로, 더 늦은 from 기준으로 시작
		LocalDateTime adjustedFrom = joinDate.isAfter(fromTime) ? joinDate : fromTime;

		// 소비로 인정되는 거래 유형: 출금, 아이템 구매
		List<TransactionSourceType> types = List.of(
			TransactionSourceType.WITHDRAWAL,
			TransactionSourceType.ITEM_PURCHASE
		);

		// 소비 총합 조회 (클래스 계좌로 보낸 금액)
		Integer result = historyRepository.sumConsumptionAmountByMemberAndDateRange(
			memberId, classId, types, adjustedFrom, toTime
		);

		return result != null ? result : 0;
	}

	/**
	 * 해당 클래스 입장 이후부터 지금까지의 누적 소비 총합을 반환합니다.
	 * - 거래 유형: 출금(WITHDRAWAL), 아이템 구매(ITEM_PURCHASE)
	 */
	@Override
	public int getTotalAccumulatedConsumption(int memberId, int classId) {

		// 해당 반 입장 시각
		LocalDateTime joinDate = memberClassroomRepository
			.findByMemberIdAndClassroomId(memberId, classId)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND))
			.getJoinDate();

		LocalDateTime now = LocalDateTime.now();

		List<TransactionSourceType> types = List.of(
			TransactionSourceType.WITHDRAWAL,
			TransactionSourceType.ITEM_PURCHASE
		);

		// 입장 이후의 모든 소비 합산
		Integer result = historyRepository.sumConsumptionAmountByMemberAndDateRange(
			memberId, classId, types, joinDate, now
		);

		return result != null ? result : 0;
	}
}
