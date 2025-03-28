package com.lufin.server.mission.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.lufin.server.mission.domain.MissionImage;

public class MissionRequestDto {
	// TODO: record를 활용해 request dto 작성
	public record MissionPostRequestDto(
		String title,
		String content,
		List<MissionImage> images,
		Integer difficulty,
		Integer maxParticipants,
		Integer wage,
		LocalDateTime missionDate
	) {

	}

}
