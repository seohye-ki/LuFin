package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

public class StockPortfolioResponseDto {
	public record PortfolioInfoDto(
		Integer stockProductId,
		Integer quantity,
		Integer totalPurchaseAmount,
		Integer totalSellAmount,
		Double totalReturn,
		Double totalReturnRate,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime createdAt,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime updatedAt
	) {
		@QueryProjection
		public PortfolioInfoDto {
		}
	}
}
