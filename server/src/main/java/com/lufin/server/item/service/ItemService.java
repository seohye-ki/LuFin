package com.lufin.server.item.service;

import java.util.List;

import com.lufin.server.item.dto.ItemDto;
import com.lufin.server.item.dto.ItemResponseDto;
import com.lufin.server.member.domain.Member;

public interface ItemService {

	ItemResponseDto createItem(ItemDto request, Integer classId);

	List<ItemResponseDto> getItems(Member member, Integer classId);

	ItemResponseDto getItemDetail(Integer itemId, Integer classId);

	ItemResponseDto updateItem(Integer itemId, ItemDto request, Integer classId);

	void deleteItem(Integer itemId, Integer classId);

	void expireItems();
}
