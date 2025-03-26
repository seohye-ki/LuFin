package com.lufin.server.item.service;

import com.lufin.server.item.domain.ItemPurchase;
import com.lufin.server.item.dto.ItemPurchaseRequestDto;
import com.lufin.server.member.domain.Member;

public interface ItemPurchaseService {

	ItemPurchase purchaseItem(ItemPurchaseRequestDto request, Member student);

}
