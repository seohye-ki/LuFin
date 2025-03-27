package com.lufin.server.item.dto;

import java.time.LocalDateTime;

import com.lufin.server.item.domain.ItemPurchase;
import com.lufin.server.item.domain.ItemPurchaseStatus;

public record ItemPurchaseResponseDto(
	Integer purchaseId,
	Integer itemId,
	Integer itemCount,
	Integer purchasePrice,
	ItemPurchaseStatus status,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static ItemPurchaseResponseDto from(ItemPurchase itemPurchase) {
		return new ItemPurchaseResponseDto(
			itemPurchase.getId(),
			itemPurchase.getItem().getId(),
			itemPurchase.getItemCount(),
			itemPurchase.getPurchasePrice(),
			itemPurchase.getStatus(),
			itemPurchase.getCreatedAt(),
			itemPurchase.getUpdatedAt()
		);
	}
}
