package com.lufin.server.item.service;

import org.springframework.stereotype.Service;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.item.domain.Item;
import com.lufin.server.item.domain.ItemPurchase;
import com.lufin.server.item.dto.ItemPurchaseRequestDto;
import com.lufin.server.member.domain.Member;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemPurchaseServiceImpl implements ItemPurchaseService {

	@Override
	@Transactional
	public ItemPurchase purchaseItem(ItemPurchaseRequestDto request, Member student) {
		
		// 아이템 존재 여부 확인(존재하지 않으면 오류)
		Item item = itemRepository.findById(request.itemId())
				.orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

		// 아이템 수량 확인(재고 부족시 오류)
		if(item.getQuantityAvailable() - item.getQuantitySold() < request.itemCount()) {
			throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
		}

		// TODO: 사용자 잔액 확인(잔액 부족시 에러)
	}

}
