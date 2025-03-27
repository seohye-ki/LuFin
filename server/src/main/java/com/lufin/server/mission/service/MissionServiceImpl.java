package com.lufin.server.mission.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.mission.dto.MissionResponseDto;
import com.lufin.server.mission.repository.MissionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionServiceImpl implements MissionService {

	private final MissionRepository missionRepository;

	// TODO: 캐시 추가로 조회 성능 향상 도모
	@Override
	public List<MissionResponseDto.MissionSummaryResponseDto> getAllMissions(Integer classId) {
		try {

			List<MissionResponseDto.MissionSummaryResponseDto> result = missionRepository.getAllMissions(classId);
			return result;
		} catch (Exception e) {
			//TODO: error 코드에 따른 exception 추가
			return null;
		}
	}

	@Override
	public MissionResponseDto.MissionDetailResponseDto getMissionById(Integer classId, Integer missionId) {
		log.info("미션 상세 조회 요청: classId: {}, missionId: {}", classId, missionId);

		try {
			// 선생님이면 participations가 있고, 학생이면 없음

			MissionResponseDto.MissionDetailResponseDto result = missionRepository.getMissionByIdForTeacher(classId,
				missionId);

			return result;
		} catch (Exception e) {
			//TODO: error 코드에 따른 exception 추가
			return null;
		}
	}
}
