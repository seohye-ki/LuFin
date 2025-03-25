package com.lufin.server.mission.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.classroom.domain.QClassroom;
import com.lufin.server.mission.domain.QMission;
import com.lufin.server.mission.dto.MissionResponseDto;
import com.lufin.server.mission.dto.QMissionResponseDto_MissionDetailResponseDto;
import com.lufin.server.mission.dto.QMissionResponseDto_MissionSummaryResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MissionRepositoryImpl implements MissionRepository {

	private final JPAQueryFactory queryFactory;

	/**
	 * 미션 목록 조회
	 * @param classId 클래스 고유 번호
	 * @return [{}, {}, ...]
	 */
	@Override
	public List<MissionResponseDto.MissionSummaryResponseDto> getAllMissions(Integer classId) {
		QMission mission = QMission.mission;
		// QClassroom classroom = QClassroom.classroom;

		// CHECK: classroom과 join하지 않아서 필요 없을 것으로 판단
		// BooleanBuilder builder = new BooleanBuilder();
		//
		// // classId가 존재하면 classId와 일치하는 classroom만 조회하는 조건 추가
		// if (classId != null) {
		// 	builder.and(classroom.id.eq(classId));
		// }

		// 쿼리문
		List<MissionResponseDto.MissionSummaryResponseDto> result = queryFactory
			.select(new QMissionResponseDto_MissionSummaryResponseDto(
				mission.missionId,
				mission.title,
				mission.difficulty,
				mission.maxParticipants,
				mission.currentParticipants,
				mission.wage,
				mission.missionDate,
				mission.status
			))
			.from(mission)
			.fetch();

		return result;
	}

	/**
	 * 미션 상세 조회
	 * @param classId 클래스 고유 번호
	 * @param missionId 미션 고유 번호
	 * @return {}
	 */
	@Override
	public MissionResponseDto.MissionDetailResponseDto getMissionById(Integer classId, Integer missionId) {
		QMission mission = QMission.mission;
		QClassroom classroom = QClassroom.classroom;

		BooleanBuilder builder = new BooleanBuilder();

		// classId와 일치하는 classroom만 조회하는 조건 추가
		if (classId != null) {
			builder.and(classroom.id.eq(classId));
		}

		// missionId와 일치하는 mission만 조회하는 조건 추가
		if (missionId != null) {
			builder.and(mission.missionId.eq(missionId));
		}

		MissionResponseDto.MissionDetailResponseDto result = queryFactory
			.select(new QMissionResponseDto_MissionDetailResponseDto(
				mission.missionId,
				mission.classId,
				mission.title,
				mission.content,
				mission.image, //TODO: image entity 분리에 따라 join으로 변경 필요
				mission.difficulty,
				mission.maxParticipants,
				mission.currentParticipants,
				mission.wage,
				mission.missionDate,
				mission.status,
				mission.createdAt,
				mission.updatedAt
			))
			.from(mission)
			.where(builder)
			.fetchOne();

		return null;
	}
}
