package com.lufin.server.mission.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.member.support.UserContext;
import com.lufin.server.mission.domain.Mission;
import com.lufin.server.mission.dto.MissionRequestDto;
import com.lufin.server.mission.dto.MissionResponseDto;
import com.lufin.server.mission.repository.MissionRepository;
import com.lufin.server.mission.repository.MissionUtilRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionServiceImpl implements MissionService {

	private final MissionUtilRepository missionUtilRepository;
	private final MissionRepository missionRepository;
	private final ClassroomRepository classroomRepository;

	// TODO: 추후 캐시 추가로 조회 성능 향상 도모
	@Override
	public List<MissionResponseDto.MissionSummaryResponseDto> getAllMissions(Integer classId) {
		log.info("미션 전체 조회 요청: classId: {}", classId);
		try {
			if (classId == null) {
				throw new BusinessException(MISSING_REQUIRED_VALUE);
			}

			List<MissionResponseDto.MissionSummaryResponseDto> result = missionRepository.getAllMissions(classId);
			return result;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Override
	public MissionResponseDto.MissionDetailResponseDto getMissionById(Integer classId, Integer missionId,
		Enum<MemberRole> role) {
		log.info("미션 상세 조회 요청: classId: {}, missionId: {}, role: {}", classId, missionId, role);

		if (classId == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		if (missionId == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		if (role == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		try {
			MissionResponseDto.MissionDetailResponseDto result;

			// 선생님이면 participations가 있고, 학생이면 없음
			if (role != MemberRole.TEACHER) {
				result = missionRepository.getMissionByIdForTeacher(classId,
					missionId);

			} else if (role != MemberRole.STUDENT) {
				result = missionRepository.getMissionByIdForStudent(classId,
					missionId);
			} else {
				throw new BusinessException(INVALID_ROLE_SELECTION);
			}

			return result;

		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Transactional
	@Override
	public MissionResponseDto.MissionPostResponseDto postMission(MissionRequestDto.MissionPostRequestDto requestDto,
		Integer classId) {
		log.info("미션 생성 요청: classId: {}, with request details: {}", classId, requestDto);

		if (classId == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		if (requestDto == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		try {
			// 선생님이 아닌 경우 생성 불가
			Member currentMember = UserContext.get();

			if (currentMember == null) {
				throw new BusinessException(UNAUTHORIZED_ACCESS);
			}

			if (!String.valueOf(currentMember.getMemberRole()).equals("TEACHER")) {
				throw new BusinessException(FORBIDDEN_REQUEST);
			}

			// classId로 Classroom 객체 조회
			Classroom classroom = classroomRepository.findById(classId)
				.orElseThrow(() -> new BusinessException(CLASS_NOT_FOUND));

			// Mission 엔티티 생성
			Mission newMission = Mission.create(
				classId,
				classroom,
				requestDto.title(),
				requestDto.content(),
				requestDto.difficulty(),
				requestDto.maxParticipants(),
				requestDto.wage(),
				requestDto.missionDate()
			);

			// JPA repository save() 활용해서 저장한 엔티티 조회
			Mission savedMission = missionRepository.save(newMission);

			return new MissionResponseDto.MissionPostResponseDto(savedMission.getId());
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Override
	public void deleteMission(Integer classId, Integer missionId, Enum<MemberRole> role) {
		log.info("미션 삭제 요청: classId: {}, missionId: {}, role: {}", classId, missionId, role);

		if (classId == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		if (missionId == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		if (role == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		try {
			// 선생님이 아니면 삭제 불가
			if (role != MemberRole.TEACHER) {
				throw new BusinessException(FORBIDDEN_REQUEST);
			}

			// 해당 클래스의 담당교사가 아닌 경우 삭제 불가
			if (!Objects.equals(missionUtilRepository.getClassIdByMissionId(missionId), classId)) {
				throw new BusinessException(FORBIDDEN_REQUEST);
			}

			missionUtilRepository.deleteMission(classId, missionId);

		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}
}
