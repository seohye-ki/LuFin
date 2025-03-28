package com.lufin.server.mission.dto;

import java.time.LocalDateTime;

public class MissionRequestDto {
	public record MissionPostRequestDto(
		String title,
		String content,
		Integer difficulty,
		Integer maxParticipants,
		Integer wage,
		LocalDateTime missionDate
	) {

	}

}
