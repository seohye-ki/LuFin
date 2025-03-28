package com.lufin.server.mission.repository;

import java.util.List;

import com.lufin.server.mission.dto.MissionResponseDto;

public interface MissionRepositoryCustom {
	// 미션 목록 조회
	//TODO: 쿼리 스트링으로 필터 받을 시 파라미터 수정 필요
	List<MissionResponseDto.MissionSummaryResponseDto> getAllMissions(Integer classId);

	// (선생님) 미션 상세 조회
	MissionResponseDto.MissionDetailResponseDto getMissionByIdForTeacher(Integer classId, Integer missionId);

	// (학생) 미션 상세 조회
	MissionResponseDto.MissionDetailResponseDto getMissionByIdForStudent(Integer classId, Integer missionId);
}
