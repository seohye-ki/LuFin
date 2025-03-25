package com.lufin.server.mission.repository;

import java.util.List;

import com.lufin.server.mission.domain.Mission;

public interface MissionRepository {
	// 미션 목록 조회
	//TODO: 쿼리 스트링으로 필터 받을 시 파라미터 수정 필요
	List<Mission> getAllMissions();

	// 미션 상세 조회
	Mission getMissionById(Integer id);
}
