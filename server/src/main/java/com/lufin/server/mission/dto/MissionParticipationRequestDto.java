package com.lufin.server.mission.dto;

import com.lufin.server.mission.domain.MissionParticipationStatus;

import jakarta.validation.constraints.NotBlank;

public class MissionParticipationRequestDto {
	public record MissionParticipationStatusRequestDto(
		@NotBlank(message = "상태는 필수 입력 값입니다.")
		MissionParticipationStatus status
	) {

	}
}
