package com.lufin.server.item.service;

import java.util.List;

import com.lufin.server.item.dto.ItemDto;
import com.lufin.server.item.dto.ItemPurchaseRequestDto;
import com.lufin.server.item.dto.ItemPurchaseResponseDto;
import com.lufin.server.item.dto.ItemResponseDto;
import com.lufin.server.member.domain.Member;

import jakarta.validation.Valid;

public interface ItemService {

	ItemResponseDto  createItem(ItemDto request, Member teacher);

	List<ItemResponseDto> getItems(Member member);

	ItemResponseDto getItemDetail(Integer itemId, Member teacher);

	ItemResponseDto updateItem(Integer itemId, ItemDto request, Member teacher);

	void deleteItem(Integer itemId, Member teacher);

	void expireItems();
}
