package com.lufin.server.item.dto;

import java.time.LocalDateTime;

import com.lufin.server.item.domain.ItemRequest;
import com.lufin.server.item.domain.ItemRequestStatus;

public record ItemRequestResponseDto(
	Integer requestId,
	Integer purchaseId,
	Integer requesterId,
	ItemRequestStatus status,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static ItemRequestResponseDto from(ItemRequest request) {
		return new ItemRequestResponseDto(
			request.getId(),
			request.getPurchase().getId(),
			request.getRequester().getId(),
			request.getStatus(),
			request.getCreatedAt(),
			request.getUpdatedAt()
		);
	}
}
