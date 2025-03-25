package com.lufin.server.mission.service;

import java.util.List;

import com.lufin.server.mission.domain.Mission;

public interface MissionService {
	// 미션 목록 조회
	List<Mission> getAllMissions(Integer classId);

	// 미션 상세 조회
	Mission getMissionById(Integer classId, Integer missionId);
}
