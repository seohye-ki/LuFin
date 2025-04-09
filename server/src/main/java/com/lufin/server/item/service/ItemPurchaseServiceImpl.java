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
		log.info("🔍[반 확인 시작] - classId: {}", classId);
		classroomRepository.findById(classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.CLASS_NOT_FOUND));
	}

	private Item validateItemOwnership(Integer itemId, Integer classId) {
		log.info("🔍[아이템 소유권 확인 시작] - itemId: {}, classId: {}", itemId, classId);
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
		if (!item.getClassroom().getId().equals(classId)) {
			log.warn("🚫[아이템 소유권 확인 실패] - itemId: {}, classId: {}", itemId, classId);
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		return item;
	}

	private Account getActiveAccount(Member member, int classId) {
		log.info("🔍[계좌 조회 시작] - memberId: {}", member.getId());
		return accountRepository.findOpenAccountByMemberIdWithPessimisticLock(member.getId(), classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	private Account getClassAccount(Integer classId) {
		log.info("🔍[반 계좌 조회 시작] - classId: {}", classId);
		return accountRepository.findByClassroomId(classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	private void validateItemStatus(Item item, int itemCount) {
		log.info("🔍[아이템 상태 확인] - itemId: {}, 요청 수량: {}", item.getId(), itemCount);
		if (!Boolean.TRUE.equals(item.getStatus())) {
			log.warn("🚫[아이템 비활성 상태] - itemId: {}", item.getId());
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		int availableStock = item.getQuantityAvailable() - item.getQuantitySold();
		if (availableStock < itemCount) {
			log.warn("🚫[아이템 재고 부족] - itemId: {}, 남은 수량: {}", item.getId(), availableStock);
			throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
		}
	}

	@Override
	@Transactional
	public List<ItemPurchaseResponseDto> purchaseItem(ItemPurchaseRequestDto request, Member student, Integer classId) {
		log.info("🛒[아이템 구매 시작] - memberId: {}, itemId: {}, count: {}", student.getId(), request.itemId(), request.itemCount());
		validateClassroomExists(classId);

		Item item = itemRepository.findByIdWithPessimisticLock(request.itemId())
			.orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
		validateItemStatus(item, request.itemCount());

		Account account = getActiveAccount(student, classId);
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

		log.info("✅[아이템 구매 완료] - memberId: {}, itemId: {}, count: {}", student.getId(), item.getId(), request.itemCount());
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
		log.info("🔄[아이템 환불 시작] - purchaseId: {}, memberId: {}", purchaseId, student.getId());
		ItemPurchase purchase = itemPurchaseRepository.findById(purchaseId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_RECORD_NOT_FOUND));
		if (!purchase.isPurchasedBy(student)) {
			log.warn("🚫[환불 요청자 불일치] - purchaseId: {}, memberId: {}", purchaseId, student.getId());
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		validateClassroomExists(classId);
		if (!purchase.getItem().getClassroom().getId().equals(classId)) {
			log.warn("🚫[환불 교실 불일치] - purchaseId: {}, classId: {}", purchaseId, classId);
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		if (purchase.getStatus() != ItemPurchaseStatus.BUY) {
			log.warn("🚫[환불 불가 상태] - purchaseId: {}, status: {}", purchaseId, purchase.getStatus());
			throw new BusinessException(ErrorCode.PURCHASE_STATUS_NOT_BUY);
		}

		Item item = itemRepository.findByIdWithPessimisticLock(purchase.getItem().getId())
			.orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

		purchase.refund();

		Account account = getActiveAccount(student, classId);
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

		item.decreaseQuantitySold(1);
		if (!item.isExpired() && item.getQuantityAvailable() - item.getQuantitySold() > 0) {
			item.enable();
		}

		log.info("✅[아이템 환불 완료] - purchaseId: {}, memberId: {}", purchaseId, student.getId());
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
