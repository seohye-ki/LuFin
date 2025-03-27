package com.lufin.server.item.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.item.domain.ItemPurchase;
import com.lufin.server.item.domain.ItemPurchaseStatus;
import com.lufin.server.item.domain.ItemRequest;
import com.lufin.server.item.dto.ItemRequestResponseDto;
import com.lufin.server.item.repository.ItemPurchaseRepository;
import com.lufin.server.item.repository.ItemRequestRepository;
import com.lufin.server.member.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
	private final ItemPurchaseRepository itemPurchaseRepository;
	private final ItemRequestRepository itemRequestRepository;

	@Override
	@Transactional
	public ItemRequestResponseDto requestItemUse(Integer purchaseId, Member student, Integer classroomId) {
		// 구매 내역 조회
		ItemPurchase purchase = itemPurchaseRepository.findById(purchaseId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_RECORD_NOT_FOUND));

		// 본인 구매인지 확인
		if (!purchase.isPurchasedBy(student)) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}

		// 구매 상태가 BUY인지 확인
		if (purchase.getStatus() != ItemPurchaseStatus.BUY) {
			throw new BusinessException(ErrorCode.PURCHASE_STATUS_NOT_BUY);
		}

		// 해당반의 아이템인지 확인
		if (!purchase.getItem().getClassroom().getId().equals(classroomId)) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}

		// 요청 생성 및 저장
		ItemRequest request = ItemRequest.create(purchase, student);
		itemRequestRepository.save(request);

		// 구매 상태 변경: BUY → PENDING
		purchase.pending();

		return ItemRequestResponseDto.from(request);
	}
}
