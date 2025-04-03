package com.lufin.server.item.service;

import java.util.List;

import com.lufin.server.item.dto.ItemPurchaseRequestDto;
import com.lufin.server.item.dto.ItemPurchaseResponseDto;
import com.lufin.server.member.domain.Member;

public interface ItemPurchaseService {

	List<ItemPurchaseResponseDto> purchaseItem(ItemPurchaseRequestDto request, Member student, Integer classId);

	List<ItemPurchaseResponseDto> getInventory(Member student, Integer classId);

	List<ItemPurchaseResponseDto> getItemPurchaseHistory(Integer itemId, Integer classId);

	ItemPurchaseResponseDto refundItem(Integer purchaseId, Member student, Integer classId);

	void expireItemPurchases();
}
