package com.lufin.server.item.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lufin.server.item.domain.ItemRequest;
import com.lufin.server.item.domain.ItemRequestStatus;

public record ItemRequestResponseDto(
	Integer requestId,
	Integer purchaseId,
	Integer memberId,
	String memberName,
	String memberProfileImage,
	String itemName,
	ItemRequestStatus status,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime createdAt,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime updatedAt
) {
	public static ItemRequestResponseDto from(ItemRequest request) {
		return new ItemRequestResponseDto(
			request.getId(),
			request.getPurchase().getId(),
			request.getRequester().getId(),
			request.getRequester().getName(),
			request.getRequester().getProfileImage(),
			request.getPurchase().getItem().getName(),
			request.getStatus(),
			request.getCreatedAt(),
			request.getUpdatedAt()
		);
	}
}
