package com.lufin.server.mission.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.mission.dto.MissionResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionParticipationServiceImpl implements MissionParticipationService {

	@Override
	public MissionResponseDto.MissionApplyResponseDto applyMission(Integer classId, Integer missionId,
		Member currentMember) {
		log.info("미션 참여 신청 요청: classId: {}, missionId: {}, currentMember: {}", classId, missionId, currentMember);

		if (classId == null || missionId == null || currentMember == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		if (currentMember.getMemberRole() != MemberRole.STUDENT) {
			throw new BusinessException(INVALID_ROLE_SELECTION);
		}

		try {
			//TODO: 서비스 비즈니스 로직 추가
			return null;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}
}
