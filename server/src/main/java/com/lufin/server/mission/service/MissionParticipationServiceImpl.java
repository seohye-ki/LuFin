package com.lufin.server.mission.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.mission.domain.Mission;
import com.lufin.server.mission.domain.MissionParticipation;
import com.lufin.server.mission.dto.MissionParticipationResponseDto;
import com.lufin.server.mission.repository.MissionParticipationRepository;
import com.lufin.server.mission.repository.MissionParticipationRepositoryCustom;
import com.lufin.server.mission.repository.MissionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionParticipationServiceImpl implements MissionParticipationService {

	private final MissionRepository missionRepository;
	private final MissionParticipationRepository missionParticipationRepository;
	private final MissionParticipationRepositoryCustom missionParticipationRepositoryCustom;

	/**
	 * 미션 참여 신청
	 * @param classId
	 * @param missionId
	 * @param currentMember
	 * @return
	 */
	@Override
	@Transactional
	public MissionParticipationResponseDto.MissionApplicationResponseDto applyMission(Integer classId,
		Integer missionId,
		Member currentMember) {
		log.info("미션 참여 신청 요청: classId: {}, missionId: {}, currentMember: {}", classId, missionId, currentMember);

		if (classId == null || missionId == null || currentMember == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		// 학생이 아닐 경우 신청 불가
		if (currentMember.getMemberRole() != MemberRole.STUDENT) {
			throw new BusinessException(INVALID_ROLE_SELECTION);
		}

		try {
			// 비관적 락을 통해 동시성 문제 해결 시도
			Mission mission = missionRepository.findByIdAndClassIdWithPessimisticLock(missionId, classId)
				.orElseThrow(() -> new BusinessException(MISSION_NOT_FOUND));

			// 인원수가 다 찼으면 신청 불가능
			if (mission.getCurrentParticipants() >= mission.getMaxParticipants()) {
				throw new BusinessException(MISSION_CAPACITY_FULL);
			}

			// MissionParticipation 엔티티 생성
			MissionParticipation newParticipation = MissionParticipation.create(
				mission,
				currentMember,
				currentMember.getId()
			);

			MissionParticipation savedParticipation = missionParticipationRepository.save(newParticipation);

			return new MissionParticipationResponseDto.MissionApplicationResponseDto(
				savedParticipation.getParticipationId());
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	/**
	 * 미션 참여자 목록 전체 조회
	 * @param classId
	 * @param missionId
	 * @param currentMember
	 * @return
	 */
	@Override
	public List<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto> getAllMissionParticipants(
		Integer classId,
		Integer missionId,
		Member currentMember
	) {
		log.info("미션 참여자 전체 목록 조회 요청: classId: {}, missionId: {}", classId, missionId);

		if (classId == null || missionId == null || currentMember == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		try {
			List<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto> missionParticipants = missionParticipationRepositoryCustom.getMissionParticipationList(
				classId,
				missionId
			);

			return missionParticipants;
		} catch (RuntimeException e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}

	}
}
