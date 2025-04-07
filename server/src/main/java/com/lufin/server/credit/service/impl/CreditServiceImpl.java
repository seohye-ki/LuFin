package com.lufin.server.credit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.credit.domain.CreditEventType;
import com.lufin.server.credit.domain.CreditScore;
import com.lufin.server.credit.dto.CreditHistoryDto;
import com.lufin.server.credit.dto.CreditScoreDto;
import com.lufin.server.credit.repository.CreditScoreHistoryRepository;
import com.lufin.server.credit.repository.CreditScoreRepository;
import com.lufin.server.credit.service.CreditScoreService;
import com.lufin.server.credit.service.CreditService;
import com.lufin.server.member.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreditServiceImpl implements CreditService {

	private final CreditScoreRepository creditRepository;
	private final CreditScoreHistoryRepository historyRepository;
	private final MemberClassroomRepository memberClassroomRepository;
	private final CreditScoreService creditScoreService;

	@Override
	public int getScore(int memberId, int classId) {
		log.info("[신용 점수 조회] memberId: {}, classId: {}", memberId, classId);
		return creditRepository.findByMemberIdAndClassId(memberId, classId)
			.map(c -> c.getScore().intValue())
			.orElse(0);
	}

	@Override
	public String getGrade(int memberId, int classId) {
		log.info("[신용 등급 조회] memberId: {}", memberId);
		return creditRepository.findByMemberIdAndClassId(memberId, classId)
			.map(score -> score.getGrade().getDisplay())
			.orElse("N/A");
	}

	@Override
	public List<CreditHistoryDto> getGradeChangeHistory(int memberId, int classId) {
		log.info("[신용 점수 이력 조회] memberId: {}", memberId);
		return historyRepository.findTop10ByMemberIdAndClassIdOrderByCreatedAtDesc(memberId, classId).stream()
			.map(h -> CreditHistoryDto.builder()
				.scoreChange(h.getScoreChange())
				.reason(h.getReason())
				.changedAt(h.getCreatedAt().toLocalDate())
				.build())
			.toList();
	}

	@Override
	@Transactional
	public CreditScoreDto recoverCreditStatus(Member teacher, int memberId, int classId) {
		MemberClassroom classroom = memberClassroomRepository.findByMember_IdAndIsCurrentTrue(memberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.STUDENT_NOT_IN_TEACHER_CLASS));
		if (classroom.getClassroom().getId() != classId) {
			throw new BusinessException(ErrorCode.STUDENT_NOT_IN_TEACHER_CLASS);
		}
		log.info("✅[반 소속 확인 완료] - memberId: {}, classId: {}", memberId, classId);

		CreditScore score = creditRepository.findByMemberId(memberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

		if (score.getCreditStatus() != 1) {
			throw new BusinessException(ErrorCode.CREDIT_RECOVERY_NOT_ALLOWED);
		}

		int delta = 39 - getScore(memberId);
		creditScoreService.applyScoreChange(score.getMember(), delta, CreditEventType.SYSTEM_RECOVERY);
		score.updateCreditStatusDescription("개인 회생 진행");

		creditRepository.save(score);

		return CreditScoreDto.from(score);
	}
}
