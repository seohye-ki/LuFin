package com.lufin.server.mission.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.member.domain.QMember;
import com.lufin.server.mission.domain.QMissionParticipation;
import com.lufin.server.mission.dto.MissionParticipationResponseDto;
import com.lufin.server.mission.dto.QMissionParticipationResponseDto_MissionParticipationSummaryResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MissionParticipationRepositoryCustomImpl implements MissionParticipationRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	// 미션 참여자 목록 조회
	@Override
	public List<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto> getMissionParticipationList(
		Integer classId,
		Integer missionId
	) {
		QMissionParticipation missionParticipation = QMissionParticipation.missionParticipation;
		QMember member = QMember.member;

		BooleanBuilder builder = new BooleanBuilder();

		// missionId에 일치하는 것만 조회하는 조건
		builder.and(missionParticipation.mission.id.eq(missionId));

		// classId에 일치하는 것만 조회하는 조건
		builder.and(missionParticipation.mission.classId.eq(classId));

		// 쿼리문
		List<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto> result = queryFactory
			.select(new QMissionParticipationResponseDto_MissionParticipationSummaryResponseDto(
					member.name,
					member.profileImage,
					member.status.creditRating,
					missionParticipation.status
				)
			)
			.from(missionParticipation)
			.leftJoin(missionParticipation.member, member).fetchJoin()
			.where(builder)
			.fetch();

		return result;
	}

}
