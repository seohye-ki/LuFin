package com.lufin.server.mission.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.lufin.server.mission.dto.QMissionResponseDto_MissionSummaryResponseDto is a Querydsl Projection type for MissionSummaryResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMissionResponseDto_MissionSummaryResponseDto extends ConstructorExpression<MissionResponseDto.MissionSummaryResponseDto> {

    private static final long serialVersionUID = 957693995L;

    public QMissionResponseDto_MissionSummaryResponseDto(com.querydsl.core.types.Expression<Integer> missionId, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<Integer> difficulty, com.querydsl.core.types.Expression<Integer> maxParticipants, com.querydsl.core.types.Expression<Integer> currentParticipants, com.querydsl.core.types.Expression<Integer> wage, com.querydsl.core.types.Expression<java.time.LocalDateTime> missionDate, com.querydsl.core.types.Expression<com.lufin.server.mission.domain.MissionStatus> status) {
        super(MissionResponseDto.MissionSummaryResponseDto.class, new Class<?>[]{int.class, String.class, int.class, int.class, int.class, int.class, java.time.LocalDateTime.class, com.lufin.server.mission.domain.MissionStatus.class}, missionId, title, difficulty, maxParticipants, currentParticipants, wage, missionDate, status);
    }

}

