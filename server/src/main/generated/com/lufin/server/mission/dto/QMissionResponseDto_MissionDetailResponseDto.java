package com.lufin.server.mission.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.lufin.server.mission.dto.QMissionResponseDto_MissionDetailResponseDto is a Querydsl Projection type for MissionDetailResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMissionResponseDto_MissionDetailResponseDto extends ConstructorExpression<MissionResponseDto.MissionDetailResponseDto> {

    private static final long serialVersionUID = -1384643430L;

    public QMissionResponseDto_MissionDetailResponseDto(com.querydsl.core.types.Expression<Integer> missionId, com.querydsl.core.types.Expression<Integer> classId, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<? extends java.util.List<com.lufin.server.mission.domain.MissionImage>> images, com.querydsl.core.types.Expression<Integer> difficulty, com.querydsl.core.types.Expression<Integer> maxParticipants, com.querydsl.core.types.Expression<Integer> currentParticipants, com.querydsl.core.types.Expression<? extends java.util.List<com.lufin.server.mission.domain.MissionParticipation>> participations, com.querydsl.core.types.Expression<Integer> wage, com.querydsl.core.types.Expression<java.time.LocalDateTime> missionDate, com.querydsl.core.types.Expression<com.lufin.server.mission.domain.MissionStatus> status, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt) {
        super(MissionResponseDto.MissionDetailResponseDto.class, new Class<?>[]{int.class, int.class, String.class, String.class, java.util.List.class, int.class, int.class, int.class, java.util.List.class, int.class, java.time.LocalDateTime.class, com.lufin.server.mission.domain.MissionStatus.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, missionId, classId, title, content, images, difficulty, maxParticipants, currentParticipants, participations, wage, missionDate, status, createdAt, updatedAt);
    }

}

