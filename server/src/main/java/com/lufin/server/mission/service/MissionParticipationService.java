package com.lufin.server.mission.service;

import com.lufin.server.member.domain.Member;
import com.lufin.server.mission.dto.MissionParticipationResponseDto;

public interface MissionParticipationService {
	// 미션 참여 신청
	MissionParticipationResponseDto.MissionApplicationResponseDto applyMission(
		Integer classId,
		Integer missionId,
		Member currentMember
	);

	// 미션 참여자 목록 조회
	MissionParticipationResponseDto.MissionParticipationSummaryResponseDto getAllMissionParticipants(
		Integer classId,
		Integer missionId
	);
}
