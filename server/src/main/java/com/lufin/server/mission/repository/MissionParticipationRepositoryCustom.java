package com.lufin.server.mission.repository;

import java.util.List;

import com.lufin.server.mission.dto.MissionParticipationResponseDto;

public interface MissionParticipationRepositoryCustom {
	// 미션 참여자 목록 조회
	List<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto> getMissionParticipationList(
		Integer classId,
		Integer missionId
	);
}
