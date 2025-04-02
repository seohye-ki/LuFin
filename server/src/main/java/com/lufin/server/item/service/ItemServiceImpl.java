package com.lufin.server.item.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.item.domain.Item;
import com.lufin.server.item.dto.ItemDto;
import com.lufin.server.item.dto.ItemResponseDto;
import com.lufin.server.item.repository.ItemRepository;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
	private final ItemRepository itemRepository;
	private final ClassroomRepository classroomRepository;

	private Classroom validateClassroomExists(Integer classId) {
		return classroomRepository.findById(classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.CLASS_NOT_FOUND));
	}

	private Item validateItemOwnership(Integer itemId, Integer classId) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
		if (!item.getClassroom().getId().equals(classId)) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		return item;
	}

	@Override
	@Transactional
	public ItemResponseDto createItem(ItemDto request, Integer classId) {
		Classroom classroom = validateClassroomExists(classId);
		Item item = Item.create(
			classroom,
			request.name(),
			request.type(),
			request.price(),
			request.quantityAvailable(),
			request.expirationDate()
		);
		Item savedItem = itemRepository.save(item);
		log.info("✅[아이템 생성 성공] itemId: {}, classroomId: {}", savedItem.getId(), classroom.getId());
		return ItemResponseDto.from(savedItem);
	}

	@Override
	public List<ItemResponseDto> getItems(Member member, Integer classId) {
		validateClassroomExists(classId);
		List<Item> items;
		if (member.getMemberRole() == MemberRole.TEACHER) {
			items = itemRepository.findByClassroomId(classId);
		} else {
			items = itemRepository.findByClassroomIdAndStatusTrue(classId);
		}
		return items.stream()
			.map(ItemResponseDto::from)
			.toList();
	}

	@Override
	public ItemResponseDto getItemDetail(Integer itemId, Integer classId) {
		validateClassroomExists(classId);
		Item item = validateItemOwnership(itemId, classId);
		return ItemResponseDto.from(item);
	}

	@Override
	@Transactional
	public ItemResponseDto updateItem(Integer itemId, ItemDto request, Integer classId) {
		validateClassroomExists(classId);
		Item item = validateItemOwnership(itemId, classId);
		if (request.quantityAvailable() < item.getQuantitySold()) {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}

		item.changeName(request.name());
		item.changePrice(request.price());
		item.changeQuantityAvailable(request.quantityAvailable());
		item.changeExpirationDate(request.expirationDate());
		Item savedItem = itemRepository.save(item);
		log.info("✅[아이템 수정 성공] itemId: {}, classroomId: {}", savedItem.getId(), classId);
		return ItemResponseDto.from(savedItem);
	}

	@Override
	@Transactional
	public void deleteItem(Integer itemId, Integer classId) {
		validateClassroomExists(classId);
		Item item = validateItemOwnership(itemId, classId);
		itemRepository.delete(item);
		log.info("✅[아이템 삭제 성공] itemId: {}, classroomId: {}", item.getId(), classId);
	}

	@Override
	@Transactional
	public void expireItems() {
		log.info("🔔[만료 아이템 검사 시작]");
		List<Item> activeItems = itemRepository.findByStatusTrue();
		for (Item item : activeItems) {
			if (item.isExpired()) {
				item.disable();
				log.info("⌛[아이템 만료 처리] itemId: {}, classroomId: {}", item.getId(), item.getClassroom().getId());
			}
		}
		log.info("✅[만료 아이템 검사 종료]");
	}
}
