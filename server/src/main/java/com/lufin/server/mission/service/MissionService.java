package com.lufin.server.mission.service;

import java.util.List;

import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.mission.dto.MissionRequestDto;
import com.lufin.server.mission.dto.MissionResponseDto;

public interface MissionService {
	// 미션 목록 조회
	List<MissionResponseDto.MissionSummaryResponseDto> getAllMissions(Integer classId);

	// 미션 상세 조회
	MissionResponseDto.MissionDetailResponseDto getMissionById(Integer classId, Integer missionId,
		Enum<MemberRole> role);

	// 미션 등록
	MissionResponseDto.MissionPostResponseDto postMission(MissionRequestDto.MissionRequestInfoDto requestDto,
		Integer classId);

	// 미션 삭제
	void deleteMission(Integer classId, Integer missionId, Enum<MemberRole> role);

	// 미션 수정
	MissionResponseDto.MissionDetailResponseDto putMission(MissionRequestDto.MissionRequestInfoDto requestDto,
		Integer classId, Integer missionId, Enum<MemberRole> role);
}
