package com.lufin.server.stock.dto;

import jakarta.validation.constraints.NotBlank;

public class StockNewsRequestDto {
	public record NewsInfoDto(
		@NotBlank(message = "내용은 반드시 채워야합니다.")
		String content
	) {

	}

	public record NewsAiDto(
		Integer id,
		String content
	) {

	}

	public record NewsAiResponseDto(
		NewsAiDto data
	) {

	}

}
