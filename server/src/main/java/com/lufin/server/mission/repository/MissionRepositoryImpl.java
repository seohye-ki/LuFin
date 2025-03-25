package com.lufin.server.mission.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.mission.domain.Mission;

@Repository
public class MissionRepositoryImpl implements MissionRepository {

	/**
	 * 미션 목록 조회
	 * @return [{}, {}, ...]
	 */
	@Override
	public List<Mission> getAllMissions() {
		// TODO: 서비스에서 받아온 값으로 수정
		return List.of();
	}

	/**
	 * 미션 상세 조회
	 * @param id
	 * @return {}
	 */
	@Override
	public Mission getMissionById(Integer id) {
		// TODO: 서비스에서 받아온 값으로 수정
		return null;
	}
}
