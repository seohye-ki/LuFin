package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lufin.server.stock.domain.StockPriceHistory;

public class StockPriceHistoryResponseDto {
	public record PriceHistoryResponseDto(
		Integer stockHistoryId,
		Integer stockProductId,
		Integer unitPrice,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime createdAt
	) {
		// StockPriceHistory 엔티티를 dto로 변환하는 메서드
		public static PriceHistoryResponseDto stockPriceHistoryEntityToPriceHistoryResponseDto(
			StockPriceHistory stockPriceHistory
		) {
			return new PriceHistoryResponseDto(
				stockPriceHistory.getId(),
				stockPriceHistory.getStockProduct().getId(),
				stockPriceHistory.getUnitPrice(),
				stockPriceHistory.getCreatedAt()
			);
		}
	}
}
