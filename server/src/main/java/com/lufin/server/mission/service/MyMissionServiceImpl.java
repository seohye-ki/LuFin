package com.lufin.server.mission.service;

import static com.lufin.server.mission.domain.MissionParticipationStatus.*;

import java.util.List;

import org.springframework.stereotype.Service;

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
	public List<MyMissionDto> getMyMissions(int memberId, int classId) {
		log.info("[미션 지원 내역 조회] memberId={}, classId={}", memberId, classId);
		List<MyMissionDto> missions = myMissionRepository.findMyMissions(memberId, classId);
		log.info("[미션 조회] 결과: {} 건", missions.size());
		return missions;
	}

	@Override
	public int getCompletedCount(int memberId, int classId) {
		log.info("[완료한 미션 수 조회]: memberId={}", memberId);
		return myMissionRepository.countByMemberIdAndClassIdAndStatus(memberId, classId,
			MissionParticipationStatus.SUCCESS);
	}

	@Override
	public int getTotalWage(int memberId, int classId) {
		log.info("[총 지급받은 보수 조회]: memberId={}", memberId);
		List<MyMissionDto> successMissions = myMissionRepository.findAllByMemberIdAndClassIdAndStatus(
			memberId, classId, MissionParticipationStatus.SUCCESS);

		return successMissions.stream()
			.mapToInt(MyMissionDto::wage)
			.sum();
	}

	@Override
	public List<MyMissionDto> hasOngoingMission(int memberId, int classId) {
		log.info("[현재 진행중인 미션 조회]: memberId={}, classId={}", memberId, classId);
		return myMissionRepository.findAllByMemberIdAndClassIdAndStatus(memberId, classId, IN_PROGRESS);
	}
}
