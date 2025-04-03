package com.lufin.server.mission.dto;

import java.time.LocalDateTime;

import com.lufin.server.mission.domain.MissionParticipationStatus;

public record MyMissionDto(int missionId,
						   int participationId,
						   String title,
						   MissionParticipationStatus status,
						   int wage,
						   LocalDateTime missionDate
) {
}
