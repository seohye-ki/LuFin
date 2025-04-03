package com.lufin.server.item.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.constants.HistoryStatus;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.item.domain.Item;
import com.lufin.server.item.domain.ItemPurchase;
import com.lufin.server.item.domain.ItemPurchaseStatus;
import com.lufin.server.item.dto.ItemDashboardDto;
import com.lufin.server.item.dto.ItemPurchaseRequestDto;
import com.lufin.server.item.dto.ItemPurchaseResponseDto;
import com.lufin.server.item.repository.ItemPurchaseRepository;
import com.lufin.server.item.repository.ItemRepository;
import com.lufin.server.member.domain.Member;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.domain.TransactionType;
import com.lufin.server.transaction.service.TransactionHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemPurchaseServiceImpl implements ItemPurchaseService {

	private final ItemRepository itemRepository;
	private final ItemPurchaseRepository itemPurchaseRepository;
	private final ClassroomRepository classroomRepository;
	private final AccountRepository accountRepository;
	private final TransactionHistoryService transactionHistoryService;

	private void validateClassroomExists(Integer classId) {
		classroomRepository.findById(classId)
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

	private Account getActiveAccount(Member member) {
		return accountRepository.findOpenAccountByMemberIdWithPessimisticLock(member.getId())
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	private Account getClassAccount(Integer classId) {
		return accountRepository.findByClassroomId(classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	private void validateItemStatus(Item item, int itemCount) {
		if (!Boolean.TRUE.equals(item.getStatus())) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		int availableStock = item.getQuantityAvailable() - item.getQuantitySold();
		if (availableStock < itemCount) {
			throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
		}
	}

	@Override
	@Transactional
	public List<ItemPurchaseResponseDto> purchaseItem(ItemPurchaseRequestDto request, Member student, Integer classId) {
		validateClassroomExists(classId);
		Item item = validateItemOwnership(request.itemId(), classId);
		validateItemStatus(item, request.itemCount());

		Account account = getActiveAccount(student);
		int totalPrice = item.getPrice() * request.itemCount();
		account.withdraw(totalPrice);
		Account classAccount = getClassAccount(classId);
		transactionHistoryService.record(
			account,
			classAccount.getAccountNumber(),
			student,
			totalPrice,
			account.getBalance(),
			TransactionType.WITHDRAWAL,
			HistoryStatus.SUCCESS,
			"아이템 구매",
			TransactionSourceType.ITEM_PURCHASE
		);

		item.increaseQuantitySold(request.itemCount());
		if (item.getQuantityAvailable().equals(item.getQuantitySold())) {
			item.disable();
		}

		List<ItemPurchase> purchases = new ArrayList<>();
		for (int i = 0; i < request.itemCount(); i++) {
			ItemPurchase purchase = ItemPurchase.create(item, student, 1);
			purchases.add(purchase);
		}
		itemPurchaseRepository.saveAll(purchases);

		return purchases.stream()
			.map(ItemPurchaseResponseDto::from)
			.toList();
	}

	@Override
	public List<ItemPurchaseResponseDto> getInventory(Member student, Integer classId) {
		validateClassroomExists(classId);
		List<ItemPurchase> inventory = itemPurchaseRepository.findInventory(student.getId(), ItemPurchaseStatus.BUY,
			classId);

		return inventory.stream()
			.map(ItemPurchaseResponseDto::from)
			.toList();
	}

	@Override
	public List<ItemPurchaseResponseDto> getItemPurchaseHistory(Integer itemId, Integer classId) {
		validateClassroomExists(classId);
		Item item = validateItemOwnership(itemId, classId);
		List<ItemPurchase> purchases = itemPurchaseRepository.findByItemId(item.getId());

		return purchases.stream()
			.map(ItemPurchaseResponseDto::from)
			.toList();
	}

	@Override
	@Transactional
	public ItemPurchaseResponseDto refundItem(Integer purchaseId, Member student, Integer classId) {
		ItemPurchase purchase = itemPurchaseRepository.findById(purchaseId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_RECORD_NOT_FOUND));
		if (!purchase.isPurchasedBy(student)) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		validateClassroomExists(classId);
		if (!purchase.getItem().getClassroom().getId().equals(classId)) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		if (purchase.getStatus() != ItemPurchaseStatus.BUY) {
			throw new BusinessException(ErrorCode.PURCHASE_STATUS_NOT_BUY);
		}
		purchase.refund();

		Account account = getActiveAccount(student);
		account.deposit(purchase.getPurchasePrice());
		Account classAccount = getClassAccount(classId);
		transactionHistoryService.record(
			classAccount,
			account.getAccountNumber(),
			student,
			purchase.getPurchasePrice(),
			account.getBalance(),
			TransactionType.DEPOSIT,
			HistoryStatus.SUCCESS,
			"아이템 환불",
			TransactionSourceType.REFUND
		);

		Item item = purchase.getItem();
		item.decreaseQuantitySold(1);
		if (!item.isExpired() && item.getQuantityAvailable() - item.getQuantitySold() > 0) {
			item.enable();
		}

		return ItemPurchaseResponseDto.from(purchase);
	}

	@Override
	@Transactional
	public void expireItemPurchases() {
		List<ItemPurchase> purchases = itemPurchaseRepository.findAllByStatus(ItemPurchaseStatus.BUY);

		for (ItemPurchase purchase : purchases) {
			LocalDateTime expiration = purchase.getItem().getExpirationDate();
			if (expiration.isBefore(LocalDateTime.now())) {
				purchase.expired();
			}
		}
	}

	@Override
	public List<ItemDashboardDto> getMyItems(int memberId, int classroomId) {
		log.info("[내 아이템 조회] memberId: {} classroomId: {}", memberId, classroomId);
		return itemPurchaseRepository.findInventory(memberId, ItemPurchaseStatus.BUY, classroomId)
			.stream()
			.map(ip -> {
				String name = ip.getItem().getName();
				int quantity = ip.getItemCount();
				int expireInDays = (int)ChronoUnit.DAYS.between(LocalDate.now(), ip.getItem().getExpirationDate());

				return ItemDashboardDto.builder()
					.name(name)
					.quantity(quantity)
					.expireInDays(expireInDays)
					.build();
			})
			.toList();
	}
}
