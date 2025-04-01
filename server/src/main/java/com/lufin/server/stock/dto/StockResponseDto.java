package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.lufin.server.stock.domain.StockProduct;
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

		/**
		 * 엔티티를 dto로 변환하는 메서드
		 */
		public static StockInfoDto stockEntityToStockInfoDto(
			StockProduct stockProduct
		) {
			return new StockInfoDto(
				stockProduct.getId(),
				stockProduct.getName(),
				stockProduct.getDescription(),
				stockProduct.getInitialPrice(),
				stockProduct.getCurrentPrice(),
				stockProduct.getCreatedAt(),
				stockProduct.getUpdatedAt()
			);
		}

	}
}
