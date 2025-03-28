package com.lufin.server.item.service;

import java.util.List;

import com.lufin.server.item.dto.ItemPurchaseRequestDto;
import com.lufin.server.item.dto.ItemPurchaseResponseDto;
import com.lufin.server.member.domain.Member;

public interface ItemPurchaseService {

	List<ItemPurchaseResponseDto> purchaseItem(ItemPurchaseRequestDto request, Member student);

	List<ItemPurchaseResponseDto> getInventory(Member student);

	List<ItemPurchaseResponseDto> getItemPurchaseHistory(Integer itemId, Member teacher);

	ItemPurchaseResponseDto refundItem(Integer purchaseId, Member student);

	void expireItemPurchases();
}
