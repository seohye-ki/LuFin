package com.lufin.server.mission.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.mission.domain.Mission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionServiceImpl implements MissionService {

	@Override
	public List<Mission> getAllMissions(Integer classId) {

		return List.of();
	}

	@Override
	public Mission getMissionById(Integer classId, Integer missionId) {
		return null;
	}
}
