package com.lufin.server.mission.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.lufin.server.mission.dto.QMissionParticipationResponseDto_MissionParticipationStatusResponseDto is a Querydsl Projection type for MissionParticipationStatusResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMissionParticipationResponseDto_MissionParticipationStatusResponseDto extends ConstructorExpression<MissionParticipationResponseDto.MissionParticipationStatusResponseDto> {

    private static final long serialVersionUID = -1217096433L;

    public QMissionParticipationResponseDto_MissionParticipationStatusResponseDto(com.querydsl.core.types.Expression<com.lufin.server.mission.domain.MissionParticipationStatus> status) {
        super(MissionParticipationResponseDto.MissionParticipationStatusResponseDto.class, new Class<?>[]{com.lufin.server.mission.domain.MissionParticipationStatus.class}, status);
    }

}

