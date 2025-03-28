package com.lufin.server.mission.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class MissionRequestDto {
	public record MissionRequestInfoDto(
		@NotBlank(message = "제목은 필수 입력값입니다.")
		@Size(max = 100, message = "제목은 100자를 넘을 수 없습니다.")
		String title,

		@NotBlank(message = "내용은 필수 입력값입니다.")
		String content,

		@NotNull(message = "난이도는 필수 입력값입니다.")
		@Min(value = 1, message = "난이도는 1 이상이어야 합니다.")
		@Max(value = 5, message = "난이도는 5 이하여야 합니다.")
		Integer difficulty,

		@NotNull(message = "최대 참가자 수는 필수 입력값입니다.")
		@Min(value = 1, message = "최대 참가자 수는 최소 1명 이상이어야 합니다.")
		Integer maxParticipants,

		@NotNull(message = "보상은 필수 입력값입니다.")
		@PositiveOrZero(message = "보상은 0 이상이어야 합니다.")
		Integer wage,

		@NotNull(message = "미션 날짜는 필수 입력값입니다.")
		@Future(message = "미션 날짜는 현재보다 미래여야 합니다.")
		LocalDateTime missionDate
	) {

	}

}
