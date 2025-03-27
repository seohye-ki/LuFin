package com.lufin.server.item.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.domain.AccountType;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.constants.HistoryStatus;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.item.domain.Item;
import com.lufin.server.item.domain.ItemPurchase;
import com.lufin.server.item.domain.ItemPurchaseStatus;
import com.lufin.server.item.dto.ItemPurchaseRequestDto;
import com.lufin.server.item.dto.ItemPurchaseResponseDto;
import com.lufin.server.item.dto.ItemResponseDto;
import com.lufin.server.item.repository.ItemPurchaseRepository;
import com.lufin.server.item.repository.ItemRepository;
import com.lufin.server.member.domain.Member;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.domain.TransactionType;
import com.lufin.server.transaction.service.TransactionHistoryService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemPurchaseServiceImpl implements ItemPurchaseService {

	private final ItemRepository itemRepository;
	private final ItemPurchaseRepository itemPurchaseRepository;
	private final MemberClassroomRepository memberClassroomRepository;
	private final AccountRepository accountRepository;
	private final TransactionHistoryService transactionHistoryService;

	// 현재 활성화된 클래스 찾기
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

	// 현재 활성화된 계좌 찾기
	// TODO: 찾는 로직 리팩토링 필요(filter -> JPA메서드)
	private Account getActiveAccount(Member member, Classroom classroom) {
		return accountRepository.findByMemberId(member.getId())
			.stream()
			.filter(account -> account.getType() == AccountType.DEPOSIT) // 예금 계좌인지
			.filter(account -> account.getClassroom() != null && account.getClassroom().getId().equals(classroom.getId())) // 활성화 반인지
			.filter(account -> !account.isClosed()) // 활성화된 계좌인지
			.findFirst()
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	// 멤버가 속한 반의 클래스계좌
	private Account getClassAccount(Classroom classroom) {
		return accountRepository.findByClassroomId(classroom.getId())
			.filter(account -> account.getType() == AccountType.CLASSROOM)
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	// 아이템 판매 상태 확인 및 재고 검사
	private void validateItemStatus(Item item, int itemCount) {
		if (!item.getStatus()) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		int availableStock = item.getQuantityAvailable() - item.getQuantitySold();
		if (availableStock < itemCount) {
			throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
		}
	}
	
	// TODO: 동시성 처리 로직 추가하기
	// 아이템 구매
	@Override
	@Transactional
	public List<ItemPurchaseResponseDto> purchaseItem(ItemPurchaseRequestDto request, Member student) {
		// 1. 소속 반 확인
		Classroom classroom = getActiveClassroom(student);

		// 2. 아이템 유효성 검사 및 찾기
		Item item = validateItemOwnership(request.itemId(), classroom);

		// 3. 아이템 구매 가능 여부 검사
		validateItemStatus(item, request.itemCount());

		// 4. 계좌 확인 후 차감
		Account account = getActiveAccount(student, classroom);
		int totalPrice = item.getPrice() * request.itemCount();
		account.withdraw(totalPrice);
		Account classAccount = getClassAccount(classroom);
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

		// 5. 재고 차감
		item.increaseQuantitySold(request.itemCount());

		// 6. 아이템 구매 기록 저장
		List<ItemPurchase> purchases = new ArrayList<>();
		for (int i = 0; i < request.itemCount(); i++) {
			ItemPurchase purchase = ItemPurchase.create(item, student, 1);
			purchases.add(purchase);
		}
		itemPurchaseRepository.saveAll(purchases);

		return purchases.stream()
			.map(ItemPurchaseResponseDto::from)
			.collect(Collectors.toList());
	}

	// 인벤토리 조회
	@Override
	public List<ItemPurchaseResponseDto> getInventory(Member student) {
		Classroom classroom = getActiveClassroom(student);
		List<ItemPurchase> inventory = itemPurchaseRepository.findInventory(student.getId(), ItemPurchaseStatus.BUY, classroom.getId());

		return inventory.stream()
			.map(ItemPurchaseResponseDto::from)
			.collect(Collectors.toList());
	}

	// 특정 아이템의 구매 내역 조회
	@Override
	public List<ItemPurchaseResponseDto> getItemPurchaseHistory(Integer itemId, Member teacher) {
		Classroom classroom = getActiveClassroom(teacher);
		Item item = validateItemOwnership(itemId, classroom);
		List<ItemPurchase> purchases = itemPurchaseRepository.findByItemId(item.getId());

		return purchases.stream()
			.map(ItemPurchaseResponseDto::from)
			.collect(Collectors.toList());
	}

	// 아이템 환불
	@Override
	@Transactional
	public ItemPurchaseResponseDto refundItem(Integer purchaseId, Member student) {
		// 1. 구매 기록 조회
		ItemPurchase purchase = itemPurchaseRepository.findById(purchaseId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_RECORD_NOT_FOUND));

		// 2. 본인 구매인지 확인
		if (!purchase.isPurchasedBy(student)) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}

		// 3. 활성화된 반과 일치하는지 확인
		Classroom classroom = getActiveClassroom(student);
		if (!purchase.getItem().getClassroom().getId().equals(classroom.getId())) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}

		// 4. 환불 가능 상태 체크
		if (purchase.getStatus() != ItemPurchaseStatus.BUY) {
			throw new BusinessException(ErrorCode.INVALID_REFUND_CONDITION);
		}

		// 5. 환불 처리
		purchase.refund();

		// 6. 금액 환급
		Account account = getActiveAccount(student, classroom);
		account.deposit(purchase.getPurchasePrice());
		Account classAccount = getClassAccount(classroom);
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

		// 7. 재고 복원
		purchase.getItem().decreaseQuantitySold(1);

		return ItemPurchaseResponseDto.from(purchase);
	}

}
