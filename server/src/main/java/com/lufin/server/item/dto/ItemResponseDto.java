package com.lufin.server.item.dto;

import java.time.LocalDateTime;

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
	LocalDateTime expirationDate,
	LocalDateTime createdAt,
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
