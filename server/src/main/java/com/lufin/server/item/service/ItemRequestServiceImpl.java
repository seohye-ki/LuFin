package com.lufin.server.item.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.item.domain.ItemPurchase;
import com.lufin.server.item.domain.ItemPurchaseStatus;
import com.lufin.server.item.domain.ItemRequest;
import com.lufin.server.item.domain.ItemRequestStatus;
import com.lufin.server.item.dto.ItemRequestApprovalDto;
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

	@Override
	public List<ItemRequestResponseDto> getItemRequests(Integer classroomId) {
		// 해당반의 요청 PENDING 목록 조회
		List<ItemRequest> pendingRequests = itemRequestRepository
			.findByClassroomIdAndStatus(classroomId, ItemRequestStatus.PENDING);

		return pendingRequests.stream()
			.map(ItemRequestResponseDto::from)
			.toList();
	}

	@Override
	@Transactional
	public ItemRequestResponseDto updateItemRequestStatus(Integer requestId, ItemRequestApprovalDto requestDto, Member member, Integer classroomId) {
		ItemRequest request = itemRequestRepository.findById(requestId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

		// 요청이 해당 교사의 반에 속한 요청인지 검증
		if (!request.getPurchase().getItem().getClassroom().getId().equals(classroomId)) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}

		// 상태가 PENDING이 아니면 수정 불가
		if (request.getStatus() != ItemRequestStatus.PENDING) {
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}

		ItemRequestStatus status = requestDto.status();
		if (ItemRequestStatus.APPROVED.equals(status)) {
			request.approve(member);
			request.getPurchase().used();
		} else if (ItemRequestStatus.REJECTED.equals(status)) {
			request.reject(member);
			request.getPurchase().buy();
		} else {
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}

		return ItemRequestResponseDto.from(request);
	}
}
