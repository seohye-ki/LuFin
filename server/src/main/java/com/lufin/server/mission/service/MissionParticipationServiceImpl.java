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
import com.lufin.server.mission.domain.MissionParticipationStatus;
import com.lufin.server.mission.dto.MissionParticipationResponseDto;
import com.lufin.server.mission.repository.MissionParticipationRepository;
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
			List<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto> missionParticipants = missionParticipationRepository.getMissionParticipationList(
				classId,
				missionId
			);

			return missionParticipants;
		} catch (RuntimeException e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}

	}

	/**
	 * 미션 참여 상태 변경
	 * @param classId
	 * @param missionId
	 * @param participationId
	 * @param currentMember
	 * @param status
	 * @return
	 */
	@Transactional
	@Override
	public MissionParticipationResponseDto.MissionParticipationStatusResponseDto changeMissionParticipationStatus(
		Integer classId,
		Integer missionId,
		Integer participationId,
		Member currentMember,
		MissionParticipationStatus status
	) {
		log.info("미션 참여 상태 변경 요청: classId: {}, missionId: {}, currentMember: {}, status: {}", classId, missionId,
			currentMember, status);

		if (classId == null || missionId == null || currentMember == null || status == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		// 선생님이 아니면 상태 변경 불가
		if (currentMember.getMemberRole() != MemberRole.TEACHER) {
			throw new BusinessException(FORBIDDEN_REQUEST);
		}

		try {
			MissionParticipation participation = missionParticipationRepository.findById(participationId)
				.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

			// 해당 미션 참여가 요청한 미션에 속해 있는지 확인
			Mission mission = participation.getMission();
			if (mission == null
				|| !mission.getId().equals(missionId)
				|| !mission.getClassId().equals(classId)) {
				throw new BusinessException(INVALID_INPUT_VALUE);
			}

			//CHECK: 필요하면 reject -> success로 변경이 안되는 것처럼 상태 변경 가능 여부 검증 로직 추가 필요
			// validateStatusChange(participation.getStatus(), status);

			/* JPA 더티 체킹으로 객체에 먼저 변경 사항이 캐시된 후, transaction이 끝날 때 자동으로 쿼리를 통해 DB를 업데이트 */
			participation.changeMissionStatus(status);

			// MissionParticipation 엔티티를 DTO로 변환
			return MissionParticipationResponseDto.MissionParticipationStatusResponseDto
				.missionParticipationToMissionParticipationStatusResponseDto(status);

		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	/*
	 * 상태 변경이 유효한지 검증

	private void validateStatusChange(MissionParticipationStatus current, MissionParticipationStatus target) {
		// 예시: 이미 완료된 상태는 다시 변경할 수 없음
		if (current == MissionParticipationStatus.SUCCESS || current == MissionParticipationStatus.FAILED) {
			throw new BusinessException(INVALID_STATUS_CHANGE);
		}

	}
	*/
}
