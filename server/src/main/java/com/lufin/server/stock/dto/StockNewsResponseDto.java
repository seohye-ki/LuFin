package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

public class StockNewsResponseDto {
	public record NewsInfoDto(
		Integer stockProductId,
		String content,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		@QueryProjection
		public NewsInfoDto {

		}
	}
}
