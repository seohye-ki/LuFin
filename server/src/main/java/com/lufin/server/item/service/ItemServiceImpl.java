package com.lufin.server.item.service;

import org.springframework.stereotype.Service;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.item.domain.Item;
import com.lufin.server.item.dto.ItemDto;
import com.lufin.server.item.dto.ItemResponseDto;
import com.lufin.server.item.repository.ItemRepository;
import com.lufin.server.member.domain.Member;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService{

	private final ItemRepository itemRepository;
	private final MemberClassroomRepository memberClassroomRepository;

	//  현재 활성화된 클래스 찾기
	private Classroom getActiveClassroom(Member member) {
		MemberClassroom memberClassroom = memberClassroomRepository
			.findByMember_IdAndIsCurrentTrue(member.getId())
			.orElseThrow(() -> new BusinessException(ErrorCode.REQUEST_DENIED));
		return memberClassroom.getClassroom();
	}

	// 아이템 유효성 검증 및 찾기
	private Item validateItemOwnership(Integer itemId, Classroom classroom) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
		if (!item.getClassroom().getId().equals(classroom.getId())) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		return item;
	}

	@Override
	@Transactional
	public ItemResponseDto createItem(ItemDto request, Member teacher) {
		Classroom classroom = getActiveClassroom(teacher);
		Item response = Item.create(
			classroom,
			request.name(),
			request.type(),
			request.price(),
			request.quantityAvailable(),
			request.expirationDate()
		);
		return ItemResponseDto.from(itemRepository.save(response));
	}

	@Override
	@Transactional
	public ItemResponseDto updateItem(Integer itemId, ItemDto request, Member teacher) {
		Classroom classroom = getActiveClassroom(teacher);
		Item response = validateItemOwnership(itemId, classroom);

		response.changeName(request.name());
		response.changePrice(request.price());
		response.changeQuantityAvailable(request.quantityAvailable());
		response.changeExpirationDate(request.expirationDate());
		return ItemResponseDto.from(itemRepository.save(response));
	}

	@Override
	@Transactional
	public void deleteItem(Integer itemId, Member teacher) {
		Classroom classroom = getActiveClassroom(teacher);
		Item response = validateItemOwnership(itemId, classroom);
		itemRepository.delete(response);
	}
}
