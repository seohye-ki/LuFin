package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

public class StockResponseDto {
	public record StockInfoDto(
		Integer stockProductId,
		String name,
		String description,
		Integer initialPrice,
		Integer currentPrice,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		@QueryProjection
		public StockInfoDto {
		}
	}
}
