package com.lufin.server.stock.dto;

import com.querydsl.core.annotations.QueryProjection;

public class StockTransactionResponseDto {
	public record TransactionInfoDto(
		Integer stockHistoryId
	) {
		@QueryProjection
		public TransactionInfoDto {

		}
	}
}
