package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.lufin.server.stock.domain.StockTransactionHistory;
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

		// StockTransactionHistory 엔티티를 dto로 변환하는 메서드
		public static TransactionDetailDto stockTransactionHistoryEntityToTransactionDetailDto(
			StockTransactionHistory history
		) {
			return new TransactionDetailDto(
				history.getId(),
				history.getStockProduct().getId(),
				history.getMember().getId(),
				history.getType(),
				history.getQuantity(),
				history.getPrice(),
				history.getTotalPrice(),
				history.getCreatedAt(),
				history.getUpdatedAt()
			);
		}

	}
}
