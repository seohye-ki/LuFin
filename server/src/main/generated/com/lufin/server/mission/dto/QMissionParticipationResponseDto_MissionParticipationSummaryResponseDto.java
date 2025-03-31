package com.lufin.server.mission.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.lufin.server.mission.dto.QMissionParticipationResponseDto_MissionParticipationSummaryResponseDto is a Querydsl Projection type for MissionParticipationSummaryResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMissionParticipationResponseDto_MissionParticipationSummaryResponseDto extends ConstructorExpression<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto> {

    private static final long serialVersionUID = 649752629L;

    public QMissionParticipationResponseDto_MissionParticipationSummaryResponseDto(com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> profileImage, com.querydsl.core.types.Expression<Integer> creditRating, com.querydsl.core.types.Expression<com.lufin.server.mission.domain.MissionParticipationStatus> status) {
        super(MissionParticipationResponseDto.MissionParticipationSummaryResponseDto.class, new Class<?>[]{String.class, String.class, int.class, com.lufin.server.mission.domain.MissionParticipationStatus.class}, name, profileImage, creditRating, status);
    }

}

