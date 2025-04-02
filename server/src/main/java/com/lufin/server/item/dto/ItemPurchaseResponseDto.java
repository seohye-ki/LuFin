package com.lufin.server.item.dto;

import java.time.LocalDateTime;

import com.lufin.server.item.domain.ItemPurchase;
import com.lufin.server.item.domain.ItemPurchaseStatus;
import com.lufin.server.member.domain.Member;

public record ItemPurchaseResponseDto(
	Integer purchaseId,
	Integer itemId,
	String itemName,
	Integer purchasePrice,
	ItemPurchaseStatus status,
	String memberName,
	String memberProfileImage,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	LocalDateTime expirationDate
) {
	public static ItemPurchaseResponseDto from(ItemPurchase itemPurchase) {
		Member member = itemPurchase.getMember();
		return new ItemPurchaseResponseDto(
			itemPurchase.getId(),
			itemPurchase.getItem().getId(),
			itemPurchase.getItem().getName(),
			itemPurchase.getPurchasePrice(),
			itemPurchase.getStatus(),
			member.getName(),
			member.getProfileImage(),
			itemPurchase.getCreatedAt(),
			itemPurchase.getUpdatedAt(),
			itemPurchase.getItem().getExpirationDate()
		);
	}
}
