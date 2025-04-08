package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lufin.server.stock.domain.StockNews;
import com.querydsl.core.annotations.QueryProjection;

public class StockNewsResponseDto {
	public record NewsInfoDto(
		Integer stockNewsId,
		Integer stockProductId,
		String content,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime createdAt,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime updatedAt
	) {
		@QueryProjection
		public NewsInfoDto {

		}

		// StockNews 엔티티를 dto로 변환
		public static NewsInfoDto stockNewsEntityToNewsInfoDto(
			StockNews news
		) {
			return new NewsInfoDto(news.getId(), news.getStockProduct().getId(), news.getContent(), news.getCreatedAt(),
				news.getUpdatedAt());
		}
	}

	public record NewsCreateUpdateDto(
		Integer stockNewsId
	) {
		@QueryProjection
		public NewsCreateUpdateDto {

		}

		// StockNews 엔티티를 dto로 변환
		public static NewsCreateUpdateDto stockNewsEntityToNewsCreateUpdateDto(
			StockNews news
		) {
			return new NewsCreateUpdateDto(news.getId());
		}
	}
}
