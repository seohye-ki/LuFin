package com.lufin.server.mission.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.common.annotation.TeacherOnly;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.member.support.UserContext;
import com.lufin.server.mission.domain.Mission;
import com.lufin.server.mission.domain.MissionImage;
import com.lufin.server.mission.dto.MissionRequestDto;
import com.lufin.server.mission.dto.MissionResponseDto;
import com.lufin.server.mission.repository.MissionImageRepository;
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
	private final MissionImageRepository missionImageRepository;
	private final ClassroomRepository classroomRepository;

	// TODO: 추후 캐시 추가로 조회 성능 향상 도모
	@Override
	public List<MissionResponseDto.MissionSummaryResponseDto> getAllMissions(Integer classId) {
		log.info("미션 전체 조회 요청: classId: {}", classId);
		try {
			if (classId == null) {
				log.error("미션 상세 조회에 필수 값이 누락되었습니다. classId: ");
				throw new BusinessException(MISSING_REQUIRED_VALUE);
			}

			List<MissionResponseDto.MissionSummaryResponseDto> result = missionRepository.getAllMissions(classId);
			return result;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Override
	public MissionResponseDto.MissionDetailResponseDto getMissionById(Integer classId, Integer missionId,
		MemberRole role) {
		log.info("미션 상세 조회 요청: classId: {}, missionId: {}, role: {}", classId, missionId, role);

		if (classId == null || missionId == null || role == null) {
			log.error("미션 상세 조회에 필수 값이 누락되었습니다. classId: {}, missionId: {}", classId, missionId);
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		try {
			MissionResponseDto.MissionDetailResponseDto result;

			// 선생님이면 participations가 있고, 학생이면 없음
			if (role == MemberRole.TEACHER) {
				result = missionRepository.getMissionByIdForTeacher(classId,
					missionId);

				log.info("미션 조회 완료: result = {}", result);
			} else if (role == MemberRole.STUDENT) {
				result = missionRepository.getMissionByIdForStudent(classId,
					missionId);
				log.info("미션 조회 완료: result = {}", result);
			} else {
				throw new BusinessException(INVALID_ROLE_SELECTION);
			}

			return result;

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@TeacherOnly
	@Transactional
	@Override
	public MissionResponseDto.MissionPostResponseDto postMission(MissionRequestDto.MissionRequestInfoDto requestDto,
		Integer classId) {
		log.info("미션 생성 요청: classId: {}, requestDto: {}", classId, requestDto);

		// 선생님이 아닌 경우 생성 불가
		Member currentMember = UserContext.get();

		if (classId == null || requestDto == null || currentMember == null) {
			log.error("미션 생성 요청에 필수 값이 누락되었습니다. classId: {}, requestDto: {}, currentMember: {}", classId, requestDto,
				currentMember);
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		try {
			// classId로 Classroom 객체 조회
			Classroom classroom = classroomRepository.findById(classId)
				.orElseThrow(() -> new BusinessException(CLASS_NOT_FOUND));

			// Mission 엔티티 생성
			Mission newMission = Mission.create(
				classroom,
				requestDto.title(),
				requestDto.content(),
				requestDto.difficulty(),
				requestDto.maxParticipants(),
				requestDto.wage(),
				requestDto.missionDate()
			);

			// image가 있다면 MissionImage 엔티티 생성 후 저장
			if (requestDto.s3Keys() != null && !requestDto.s3Keys().isEmpty()) {
				List<MissionImage> images = new ArrayList<>();
				for (String s3Key : requestDto.s3Keys()) {
					MissionImage newMissionImage = MissionImage.create(
						newMission,
						s3Key
					);
					images.add(newMissionImage);
				}

				missionImageRepository.saveAll(images);
			}

			// JPA repository save() 활용해서 저장한 엔티티 조회
			Mission savedMission = missionRepository.save(newMission);

			return new MissionResponseDto.MissionPostResponseDto(savedMission.getId());
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Transactional
	@TeacherOnly
	@Override
	public void deleteMission(Integer classId, Integer missionId, MemberRole role) {
		log.info("미션 삭제 요청: classId: {}, missionId: {}, role: {}", classId, missionId, role);

		if (classId == null || missionId == null || role == null) {
			log.error("미션 삭제 요청에 필수 값이 누락되었습니다. classId: {}, missionId: {}, role: {}", classId, missionId,
				role);
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		try {
			// 해당 클래스의 담당교사가 아닌 경우 삭제 불가
			if (!Objects.equals(missionUtilRepository.getClassIdByMissionId(missionId), classId)) {
				throw new BusinessException(FORBIDDEN_REQUEST);
			}

			missionUtilRepository.deleteMission(classId, missionId);

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	@Transactional
	@Override
	public MissionResponseDto.MissionDetailResponseDto putMission(
		MissionRequestDto.MissionRequestInfoDto requestDto,
		Integer classId, Integer missionId, MemberRole role) {
		log.info("미션 수정 요청: classId: {}, missionId: {}, requestDto: {}", classId, missionId, requestDto);

		if (classId == null || missionId == null || requestDto == null || role == null) {
			log.error("미션 수정 요청에 필수 값이 누락되었습니다. classId: {}, missionId: {}, requestDto: {}, role: {}",
				classId, missionId, requestDto, role);
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		// 선생님이 아니면 삭제 불가
		if (role != MemberRole.TEACHER) {
			throw new BusinessException(FORBIDDEN_REQUEST);
		}

		try {
			Mission mission = missionRepository.findById(missionId)
				.orElseThrow(() -> new BusinessException(MISSION_NOT_FOUND));

			/* JPA 더티 체킹으로 객체에 먼저 변경 사항이 캐시된 후, transaction이 끝날 때 자동으로 쿼리를 통해 DB를 업데이트 */
			mission.modifyTitle(requestDto.title());
			mission.modifyContent(requestDto.content());
			mission.modifyDifficulty(requestDto.difficulty());
			mission.modifyMaxParticipants(requestDto.maxParticipants());
			mission.modifyWage(requestDto.wage());
			mission.modifyMissionDate(requestDto.missionDate());

			// Mission 엔티티를 dto로 변환
			return MissionResponseDto.MissionDetailResponseDto.missionEntityToMissionDetailResponseDto(
				mission);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred during mission modification: {}", e.getMessage(), e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

}
