package com.lufin.server.mission.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.lufin.server.credit.domain.QCreditScore;
import com.lufin.server.member.domain.QMember;
import com.lufin.server.mission.domain.MissionParticipation;
import com.lufin.server.mission.domain.QMissionParticipation;
import com.lufin.server.mission.dto.MissionParticipationResponseDto;
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
		QCreditScore creditScore = QCreditScore.creditScore;

		/*
		 * queryDSL의 projection을 사용한 경우 entity 매핑이 정확히 되지 않는 이슈가 있었음
		 * 그에 따라 먼저 엔티티를 조회하고 DTO로 변환하여 반환하는 로직으로 변경
		 */
		List<MissionParticipation> missionParticipationEntityList = queryFactory
			.selectFrom(missionParticipation)
			.leftJoin(missionParticipation.member, member).fetchJoin()
			.leftJoin(creditScore)
			.on(
				creditScore.memberClassroom.member.id.eq(member.id)
					.and(creditScore.memberClassroom.classroom.id.eq(missionParticipation.mission.classroom.id))
			)
			.fetchJoin()
			.where(missionParticipation.mission.id.eq(missionId)
				.and(missionParticipation.mission.classroom.id.eq(classId)))
			.fetch();

		// 엔티티를 dto로 변경
		return missionParticipationEntityList.stream()
			.map(missionParticipationEntity
				-> MissionParticipationResponseDto.MissionParticipationSummaryResponseDto.missionParticipationToMissionParticipationSummaryResponseDto(
				missionParticipationEntity.getParticipationId(),
				missionParticipationEntity.getMember().getName(),
				missionParticipationEntity.getMember().getProfileImage(),
				missionParticipationEntity.getStatus()
			)).collect(Collectors.toList());
	}
}
