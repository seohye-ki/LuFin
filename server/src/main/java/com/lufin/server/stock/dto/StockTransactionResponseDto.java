package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

public class StockTransactionResponseDto {
	public record TransactionInfoDto(
		Integer stockHistoryId
	) {
		@QueryProjection
		public TransactionInfoDto {

		}
	}

	public record TransactionDetailDto(
		Integer stockHistoryId,
		Integer stockProductId,
		Integer memberId,
		Integer type,
		Integer quantity,
		Integer price,
		Integer totalPrice,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		@QueryProjection
		public TransactionDetailDto {
			
		}
	}
}
