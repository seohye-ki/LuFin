package com.lufin.server.mission.service;

import com.lufin.server.member.domain.Member;
import com.lufin.server.mission.dto.MissionResponseDto;

public interface MissionParticipationService {
	// 미션 참여 신청
	MissionResponseDto.MissionApplyResponseDto applyMission(Integer classId, Integer missionId,
		Member currentMember);
}
