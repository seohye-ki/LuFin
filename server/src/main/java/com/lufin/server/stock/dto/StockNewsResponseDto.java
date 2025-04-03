package com.lufin.server.stock.dto;

import java.time.LocalDateTime;

import com.lufin.server.stock.domain.StockNews;
import com.querydsl.core.annotations.QueryProjection;

public class StockNewsResponseDto {
	public record NewsInfoDto(
		Integer stockNewsId,
		String content,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		@QueryProjection
		public NewsInfoDto {

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
