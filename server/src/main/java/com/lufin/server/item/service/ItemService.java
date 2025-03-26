package com.lufin.server.item.service;

import com.lufin.server.item.domain.Item;
import com.lufin.server.item.dto.ItemDto;
import com.lufin.server.item.dto.ItemResponseDto;
import com.lufin.server.member.domain.Member;

public interface ItemService {

	ItemResponseDto  createItem(ItemDto request, Member teacher);

	ItemResponseDto updateItem(Integer itemId, ItemDto request, Member teacher);

	void deleteItem(Integer itemId, Member teacher);

}
