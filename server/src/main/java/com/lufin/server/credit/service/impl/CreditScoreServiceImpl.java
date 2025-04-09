package com.lufin.server.credit.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.credit.domain.CreditEventType;
import com.lufin.server.credit.domain.CreditScore;
import com.lufin.server.credit.domain.CreditScoreHistory;
import com.lufin.server.credit.repository.CreditScoreHistoryRepository;
import com.lufin.server.credit.repository.CreditScoreRepository;
import com.lufin.server.credit.service.CreditScoreService;
import com.lufin.server.member.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		// 학생의 현재 클래스 정보 조회 (해당 클래스에 속해 있어야 함)
		MemberClassroom memberClassroom = memberClassroomRepository.findByMemberIdAndClassroomId(member.getId(),
				classId)
			.orElseThrow(() -> {
				log.warn("🔁[신용 등급 반영 실패] - 학생이 해당 클래스 소속이 아님");
				return new BusinessException(STUDENT_NOT_IN_TEACHER_CLASS);
			});

		// 신용 점수 조회 (없으면 새로 초기화)
		CreditScore score = creditScoreRepository.findById(memberClassroom.getId())
			.orElseGet(() -> CreditScore.init(memberClassroom));

		// 점수 변경 및 신용 등급/상태 업데이트 + 변경 이력 생성
		CreditScoreHistory history = score.applyChange(delta, eventType);
		creditScoreRepository.save(score);
		creditScoreHistoryRepository.save(history);
	}
}
