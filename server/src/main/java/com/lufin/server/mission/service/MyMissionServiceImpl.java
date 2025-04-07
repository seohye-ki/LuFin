package com.lufin.server.mission.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.mission.domain.MissionParticipationStatus;
import com.lufin.server.mission.dto.MyMissionDto;
import com.lufin.server.mission.repository.MyMissionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyMissionServiceImpl implements MyMissionService {

	private final MyMissionRepository myMissionRepository;

	@Override
	public List<MyMissionDto> getMyMissions(int classId, int memberId) {
		log.info("[미션 지원 내역 조회] classId={}, memberId={}", classId, memberId);

		try {
			List<MyMissionDto> missions = myMissionRepository.findMyMissions(classId, memberId);
			log.info("[미션 조회] 결과: {} 건", missions.size());
			return missions;
		} catch (Exception e) {
			log.error("🎯[미션 조회 중 오류 발생] classId={}, memberId={}", classId, memberId);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Override
	public int getCompletedCount(int memberId) {
		log.info("[완료한 미션 수 조회]: memberId={}", memberId);
		return myMissionRepository.countByMemberIdAndStatus(memberId, MissionParticipationStatus.SUCCESS);
	}

	@Override
	public int getTotalWage(int memberId) {
		log.info("[총 지급받은 보수 조회]: memberId={}", memberId);
		return myMissionRepository.findAllByMemberIdAndStatus(memberId, MissionParticipationStatus.SUCCESS)
			.stream()
			.mapToInt(p -> p.getMission().getWage())
			.sum();
	}
}
