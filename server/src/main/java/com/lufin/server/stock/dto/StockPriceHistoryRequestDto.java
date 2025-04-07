package com.lufin.server.stock.dto;

public class StockPriceHistoryRequestDto {
	public record PriceHistoryAiInfoDto(
		Integer id,
		Integer previousPrice,
		Integer currentPrice,
		Double changeRate
	) {
	}

	public record PriceHistoryAiResponseDto(
		PriceHistoryAiInfoDto data
	) {
	}

}
