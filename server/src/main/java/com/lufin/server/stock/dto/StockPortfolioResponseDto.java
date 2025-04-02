package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

public class StockPortfolioResponseDto {
	public record PortfolioInfoDto(
		Integer stockProductId,
		Integer quantity,
		Integer totalPurchaseAmount,
		Integer totalSellAmount,
		Double totalReturn,
		Double totalReturnRate,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		@QueryProjection
		public PortfolioInfoDto {
		}
	}
}
