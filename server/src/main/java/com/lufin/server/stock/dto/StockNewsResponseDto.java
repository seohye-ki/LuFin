package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

public class StockNewsResponseDto {
	public record NewsInfoDto(
		Integer stockNewsId,
		String content,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		@QueryProjection
		public NewsInfoDto {

		}
	}

	public record NewsCreateUpdateDto(
		Integer stockNewsId
	) {
		@QueryProjection
		public NewsCreateUpdateDto {

		}
	}
}
