package com.lufin.server.mission.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.mission.domain.Mission;
import com.lufin.server.mission.domain.MissionParticipation;
import com.lufin.server.mission.dto.MissionResponseDto;
import com.lufin.server.mission.repository.MissionRepository;
import com.lufin.server.mission.repository.missionParticipationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionParticipationServiceImpl implements MissionParticipationService {

	private final MissionRepository missionRepository;
	private final missionParticipationRepository missionParticipationRepository;

	@Override
	public MissionResponseDto.MissionApplyResponseDto applyMission(Integer classId, Integer missionId,
		Member currentMember) {
		log.info("미션 참여 신청 요청: classId: {}, missionId: {}, currentMember: {}", classId, missionId, currentMember);

		if (classId == null || missionId == null || currentMember == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		// 학생이 아닐 경우 신청 불가
		if (currentMember.getMemberRole() != MemberRole.STUDENT) {
			throw new BusinessException(INVALID_ROLE_SELECTION);
		}

		try {
			Mission mission = missionRepository.findById(missionId)
				.orElseThrow(() -> new BusinessException(MISSION_NOT_FOUND));

			// MissionParticipation 엔티티 생성
			MissionParticipation newParticipation = MissionParticipation.create(
				mission,
				currentMember,
				currentMember.getId()
			);

			MissionParticipation savedParticipation = missionParticipationRepository.save(newParticipation);

			// TODO: response dto 작성후 해당 dto로 반환
			return null;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}
}
