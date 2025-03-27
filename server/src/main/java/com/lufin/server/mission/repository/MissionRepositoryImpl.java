package com.lufin.server.mission.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.classroom.domain.QClassroom;
import com.lufin.server.mission.domain.QMission;
import com.lufin.server.mission.domain.QMissionImage;
import com.lufin.server.mission.domain.QMissionParticipation;
import com.lufin.server.mission.dto.MissionResponseDto;
import com.lufin.server.mission.dto.QMissionResponseDto_MissionDetailResponseDto;
import com.lufin.server.mission.dto.QMissionResponseDto_MissionSummaryResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
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

		BooleanBuilder builder = new BooleanBuilder();

		// classId에 일치하는 것들만 조회하는 조건
		if (classId != null) {
			builder.and(mission.classroom.id.eq(classId));
		}

		// 쿼리문
		List<MissionResponseDto.MissionSummaryResponseDto> result = queryFactory
			.select(new QMissionResponseDto_MissionSummaryResponseDto(
				mission.id,
				mission.title,
				mission.difficulty,
				mission.maxParticipants,
				mission.currentParticipants,
				mission.wage,
				mission.missionDate,
				mission.status
			))
			.from(mission)
			.where(builder)
			.fetch();

		return result;
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
		QMissionParticipation participation = QMissionParticipation.missionParticipation;

		BooleanBuilder builder = new BooleanBuilder();

		// classId에 일치하는 것들만 조회하는 조건
		if (classId != null) {
			builder.and(mission.classroom.id.eq(classId));
		}

		// missionId와 일치하는 mission만 조회하는 조건 추가
		if (missionId != null) {
			builder.and(mission.id.eq(missionId));
		}

		MissionResponseDto.MissionDetailResponseDto result = queryFactory
			.select(new QMissionResponseDto_MissionDetailResponseDto(
				mission.id,                   // id
				mission.classId,              // classId
				mission.title,                // title
				mission.content,              // content
				mission.images,               // missionImage
				mission.difficulty,           // difficulty
				mission.maxParticipants,      // maxParticipants
				mission.currentParticipants,  // currentParticipants
				mission.participations,       // missionParticipation
				mission.wage,                 // wage
				mission.missionDate,          // missionDate
				mission.status,               // status
				mission.createdAt,            // createdAt
				mission.updatedAt             // updatedAt
			))
			.from(mission)
			.leftJoin(mission.images, missionImage).fetchJoin()
			.leftJoin(mission.participations, participation).fetchJoin()
			.where(builder)
			.fetchOne();

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
		QClassroom classroom = QClassroom.classroom;
		QMissionImage missionImage = QMissionImage.missionImage;

		BooleanBuilder builder = new BooleanBuilder();

		// classId에 일치하는 것들만 조회하는 조건
		if (classId != null) {
			builder.and(mission.classroom.id.eq(classId));
		}

		// missionId와 일치하는 mission만 조회하는 조건 추가
		if (missionId != null) {
			builder.and(mission.id.eq(missionId));
		}

		MissionResponseDto.MissionDetailResponseDto result = queryFactory
			.select(new QMissionResponseDto_MissionDetailResponseDto(
				mission.id,
				mission.classId,
				mission.title,
				mission.content,
				mission.images,
				mission.difficulty,
				mission.maxParticipants,
				mission.currentParticipants,
				Expressions.constant(new ArrayList<>()),
				mission.wage,
				mission.missionDate,
				mission.status,
				mission.createdAt,
				mission.updatedAt
			))
			.from(mission)
			.leftJoin(mission.images, missionImage).fetchJoin()
			.where(builder)
			.fetchOne();

		return result;
	}
}
