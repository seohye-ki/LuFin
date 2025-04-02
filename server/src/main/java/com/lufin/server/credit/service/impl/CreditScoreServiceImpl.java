package com.lufin.server.credit.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.credit.domain.CreditEventType;
import com.lufin.server.credit.domain.CreditScore;
import com.lufin.server.credit.domain.CreditScoreHistory;
import com.lufin.server.credit.repository.CreditScoreHistoryRepository;
import com.lufin.server.credit.repository.CreditScoreRepository;
import com.lufin.server.credit.service.CreditScoreService;
import com.lufin.server.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreditScoreServiceImpl implements CreditScoreService {

	private final CreditScoreRepository creditScoreRepository;
	private final CreditScoreHistoryRepository creditScoreHistoryRepository;

	/**
	 * 회원의 신용점수에 변동을 적용하고, 변경 이력을 저장합니다.
	 *
	 * @param member    점수를 변경할 대상 회원
	 * @param delta     변화시킬 점수 값 (양수/음수 가능)
	 * @param eventType 점수 변화의 사유를 나타내는 이벤트 타입
	 */
	@Transactional
	@Override
	public void applyScoreChange(Member member, int delta, CreditEventType eventType) {

		CreditScore score = creditScoreRepository.findById(member.getId())
			.orElseGet(() -> CreditScore.init(member));

		CreditScoreHistory history = score.applyChange(delta, eventType);
		creditScoreRepository.save(score);
		creditScoreHistoryRepository.save(history);
	}
}
