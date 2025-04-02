package com.lufin.server.stock.dto;

public class StockTransactionRequestDto {
	public record TransactionInfoDto(
		Integer type,
		Integer quantity,
		Integer price,
		Integer totalPrice
	) {

	}
}
