package com.lufin.server.credit.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
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
	private final MemberClassroomRepository memberClassroomRepository;

	/**
	 * 회원의 신용점수에 변동을 적용하고, 변경 이력을 저장합니다.
	 *
	 * @param member    점수를 변경할 대상 회원
	 * @param delta     변화시킬 점수 값 (양수: 상승, 음수: 하락)
	 * @param eventType 점수 변화의 사유를 나타내는 이벤트 타입
	 * @param classId   점수를 반영할 클래스의 ID (MemberClassroom 탐색용)
	 */
	@Transactional
	@Override
	public void applyScoreChange(Member member, int delta, CreditEventType eventType, int classId) {
		MemberClassroom memberClassroom = memberClassroomRepository.findByMemberIdAndClassroomId(member.getId(),
				classId)
			.orElseThrow(() -> new IllegalArgumentException("해당 학생은 클래스에 소속되어 있지 않습니다."));

		CreditScore score = creditScoreRepository.findById(memberClassroom.getId())
			.orElseGet(() -> CreditScore.init(memberClassroom));

		CreditScoreHistory history = score.applyChange(delta, eventType);
		creditScoreRepository.save(score);
		creditScoreHistoryRepository.save(history);
	}
}
