package com.lufin.server.mission.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lufin.server.mission.domain.MissionParticipationStatus;

public record MyMissionDto(int missionId,
						   int participationId,
						   String title,
						   MissionParticipationStatus status,
						   int wage,
						   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
						   LocalDateTime missionDate
) {
}
