package com.lufin.server.credit.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
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
		MemberClassroom mc = getMemberClassroom(memberId, classId);
		return creditRepository.findByMemberClassroom(mc)
			.map(c -> c.getScore().intValue())
			.orElse(0);
	}

	@Override
	public String getGrade(int memberId, int classId) {
		log.info("[신용 등급 조회] memberId: {}, classId: {}", memberId, classId);
		MemberClassroom mc = getMemberClassroom(memberId, classId);
		return creditRepository.findByMemberClassroom(mc)
			.map(score -> score.getGrade().getDisplay())
			.orElse("N/A");
	}

	@Override
	public List<CreditHistoryDto> getGradeChangeHistory(int memberId, int classId) {
		log.info("[신용 점수 이력 조회] memberId: {}, classId: {}", memberId, classId);
		MemberClassroom mc = getMemberClassroom(memberId, classId);
		return historyRepository.findTop10ByMemberClassroomOrderByCreatedAtDesc(mc).stream()
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
		log.info("[개인 회생 요청] - teacherId: {}, studentId: {}, classId: {}", teacher.getId(), memberId, classId);

		MemberClassroom mc = getMemberClassroom(memberId, classId);
		log.info("[반 소속 확인 완료] - studentId: {}, classId: {}", memberId, classId);

		CreditScore score = creditRepository.findByMemberClassroom(mc)
			.orElseThrow(() -> {
				log.warn("🔁[개인 회생 실패] - 신용도 조회 불가");
				return new BusinessException(CREDIT_SCORE_NOT_FOUND);
			});

		if (score.getCreditStatus() != 1) {
			log.warn("🔁[개인 회생 실패] - 회생 대상자가 아님, creditStatus: {}, score: {}, grade: {}", score.getCreditStatus(),
				score.getScore(), score.getGrade());
			throw new BusinessException(CREDIT_RECOVERY_NOT_ALLOWED);
		}

		int delta = 39 - score.getScore();
		creditScoreService.applyScoreChange(mc.getMember(), delta, CreditEventType.SYSTEM_RECOVERY, classId);
		score.updateCreditStatusDescription("[개인 회생 진행]");

		creditRepository.save(score);

		return CreditScoreDto.from(score);
	}

	private MemberClassroom getMemberClassroom(int memberId, int classId) {
		return memberClassroomRepository.findByMemberIdAndClassroomId(memberId, classId)
			.orElseThrow(() -> {
				log.warn("🔁[신용 점수 조회 실패] - 학생이 해당 클래스 소속이 아님");
				return new BusinessException(STUDENT_NOT_IN_TEACHER_CLASS);
			});
	}
}
