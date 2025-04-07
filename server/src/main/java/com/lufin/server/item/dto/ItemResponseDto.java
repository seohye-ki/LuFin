package com.lufin.server.item.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lufin.server.item.domain.Item;

public record ItemResponseDto(
	Integer id,
	Integer classroomId,
	String name,
	Integer type,
	Integer price,
	Integer quantityAvailable,
	Integer quantitySold,
	Boolean status,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime expirationDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime createdAt,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime updatedAt
) {
	public static ItemResponseDto from(Item item) {
		return new ItemResponseDto(
			item.getId(),
			item.getClassroom().getId(),
			item.getName(),
			item.getType(),
			item.getPrice(),
			item.getQuantityAvailable(),
			item.getQuantitySold(),
			item.getStatus(),
			item.getExpirationDate(),
			item.getCreatedAt(),
			item.getUpdatedAt()
		);
	}
}
