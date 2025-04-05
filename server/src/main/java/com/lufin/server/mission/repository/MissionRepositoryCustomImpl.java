package com.lufin.server.mission.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.lufin.server.mission.domain.Mission;
import com.lufin.server.mission.domain.QMission;
import com.lufin.server.mission.domain.QMissionImage;
import com.lufin.server.mission.domain.QMissionParticipation;
import com.lufin.server.mission.dto.MissionResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MissionRepositoryCustomImpl implements MissionRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	/**
	 * 미션 목록 조회
	 * @param classId 클래스 고유 번호
	 * @return [{}, {}, ...]
	 */
	@Override
	public List<MissionResponseDto.MissionSummaryResponseDto> getAllMissions(Integer classId) {
		QMission mission = QMission.mission;

		// 먼저 엔티티를 조회
		List<Mission> missions = queryFactory
			.selectFrom(mission)
			.where(mission.classId.eq(classId))
			.fetch();

		// 엔티티를 DTO로 변환
		return missions.stream()
			.map(MissionResponseDto.MissionSummaryResponseDto
				::missionEntityToMissionDetailResponseDto)
			.collect(Collectors.toList());
	}

	/**
	 * (선생님용) 미션 상세 조회
	 * @param classId 클래스 고유 번호
	 * @param missionId 미션 고유 번호
	 * @return {}
	 */
	@Override
	public MissionResponseDto.MissionDetailResponseDto getMissionByIdForTeacher(Integer classId, Integer missionId) {
		QMission mission = QMission.mission;
		QMissionImage missionImage = QMissionImage.missionImage;
		QMissionParticipation missionParticipation = QMissionParticipation.missionParticipation;

		/*
		 * queryDSL의 projection을 사용한 경우 entity 매핑이 정확히 되지 않는 이슈가 있었음
		 * 그에 따라 먼저 엔티티를 조회하고 DTO로 변환하여 반환하는 로직으로 변경
		 */
		Mission missionEntity = queryFactory
			.selectFrom(mission).distinct() // distinct()를 통해 카타시안 곱 문제 해결
			.where(mission.classId.eq(classId).and(mission.id.eq(missionId)))
			.leftJoin(mission.images, missionImage).fetchJoin()
			.leftJoin(mission.participations, missionParticipation).fetchJoin()
			.fetchOne();

		if (missionEntity == null) {
			log.warn("해당 미션을 찾을 수 없습니다. classId: {}, missionId: {}", classId, missionId);
			return null;
		}

		MissionResponseDto.MissionDetailResponseDto result = new MissionResponseDto.MissionDetailResponseDto(
			missionEntity.getId(),                   // id
			missionEntity.getClassId(),              // classId
			missionEntity.getTitle(),                // title
			missionEntity.getContent(),              // content
			missionEntity.getImages(),                 // missionImage: 데이터가 없으면 빈 리스트 반환
			missionEntity.getDifficulty(),           // difficulty
			missionEntity.getMaxParticipants(),    // maxParticipants
			missionEntity.getCurrentParticipants(),  // currentParticipants
			missionEntity.getParticipations(),        // missionParticipation: 데이터가 없으면 빈 리스트 반환
			missionEntity.getWage(),                 // wage
			missionEntity.getMissionDate(),          // missionDate
			missionEntity.getStatus(),               // status
			missionEntity.getCreatedAt(),            // createdAt
			missionEntity.getUpdatedAt()             // updatedAt
		);

		return result;
	}

	/**
	 * (학생용) 미션 상세 조회
	 * participations가 빈 값으로 전달됨
	 * @param classId
	 * @param missionId
	 * @return
	 */
	@Override
	public MissionResponseDto.MissionDetailResponseDto getMissionByIdForStudent(Integer classId, Integer missionId) {
		QMission mission = QMission.mission;
		QMissionImage missionImage = QMissionImage.missionImage;

		/*
		 * queryDSL의 projection을 사용한 경우 entity 매핑이 정확히 되지 않는 이슈가 있었음
		 * 그에 따라 먼저 엔티티를 조회하고 DTO로 변환하여 반환하는 로직으로 변경
		 */
		Mission missionEntity = queryFactory
			.selectFrom(mission)
			.where(mission.classId.eq(classId).and(mission.id.eq(missionId)))
			.leftJoin(mission.images, missionImage).fetchJoin()
			.fetchOne();

		if (missionEntity == null) {
			log.warn("해당 미션을 찾을 수 없습니다. classId: {}, missionId: {}", classId, missionId);
			return null;
		}

		MissionResponseDto.MissionDetailResponseDto result = new MissionResponseDto.MissionDetailResponseDto(
			missionEntity.getId(),                   // id
			missionEntity.getClassId(),              // classId
			missionEntity.getTitle(),                // title
			missionEntity.getContent(),              // content
			missionEntity.getImages(),               // missionImage
			missionEntity.getDifficulty(),           // difficulty
			missionEntity.getMaxParticipants(),      // maxParticipants
			missionEntity.getCurrentParticipants(),  // currentParticipants
			new ArrayList<>(),       // missionParticipation
			missionEntity.getWage(),                 // wage
			missionEntity.getMissionDate(),          // missionDate
			missionEntity.getStatus(),               // status
			missionEntity.getCreatedAt(),            // createdAt
			missionEntity.getUpdatedAt()             // updatedAt
		);

		return result;
	}

}
