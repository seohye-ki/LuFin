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
		log.info("🔍[아이템 사용 요청] - purchaseId: {}, memberId: {}, classroomId: {}", purchaseId, student.getId(),
			classroomId);
		ItemPurchase purchase = itemPurchaseRepository.findById(purchaseId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PURCHASE_RECORD_NOT_FOUND));

		if (!purchase.isPurchasedBy(student)) {
			log.warn("🧺[권한 확인 실패] 구매자 아님 - memberId: {}", student.getId());
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}

		if (purchase.getStatus() != ItemPurchaseStatus.BUY) {
			log.warn("🚫[구매 상태 오류] 사용 요청 불가 상태 - purchaseId: {}, status: {}", purchaseId, purchase.getStatus());
			throw new BusinessException(ErrorCode.PURCHASE_STATUS_NOT_BUY);
		}

		if (!purchase.getItem().getClassroom().getId().equals(classroomId)) {
			log.warn("🔐[클래스 확인 오류] 아이템의 반과 다름 - purchaseId: {}, classroomId: {}", purchaseId, classroomId);
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}

		ItemRequest request = ItemRequest.create(purchase, student);
		itemRequestRepository.save(request);
		purchase.pending();
		log.info("✅[아이템 사용 요청 생성 완료] - requestId: {}, memberId: {}", request.getId(), student.getId());
		return ItemRequestResponseDto.from(request);
	}

	@Override
	public List<ItemRequestResponseDto> getItemRequests(Integer classroomId) {
		log.info("🔄[아이템 요청 목록 조회] - classroomId: {}", classroomId);
		List<ItemRequest> pendingRequests = itemRequestRepository.findByClassroomIdAndStatus(classroomId,
			ItemRequestStatus.PENDING);
		log.info("✅[아이템 요청 목록 조회 성공] - classroomId: {}, count: {}", classroomId, pendingRequests.size());
		return pendingRequests.stream().map(ItemRequestResponseDto::from).toList();
	}

	@Override
	@Transactional
	public ItemRequestResponseDto updateItemRequestStatus(Integer requestId, ItemRequestApprovalDto requestDto,
		Member member, Integer classroomId) {
		log.info("🔧[아이템 요청 상태 변경] - requestId: {}, memberId: {}", requestId, member.getId());
		ItemRequest request = itemRequestRepository.findById(requestId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

		if (!request.getPurchase().getItem().getClassroom().getId().equals(classroomId)) {
			log.warn("🔐[클래스 확인 오류] 요청의 반과 다름 - requestId: {}, classroomId: {}", requestId, classroomId);
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}

		if (request.getStatus() != ItemRequestStatus.PENDING) {
			log.warn("🚫[요청 상태 오류] 수정 불가 상태 - requestId: {}, status: {}", requestId, request.getStatus());
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}

		ItemRequestStatus status = requestDto.status();
		if (ItemRequestStatus.APPROVED.equals(status)) {
			request.approve(member);
			request.getPurchase().used();
			log.info("✅[아이템 사용 승인 완료] - requestId: {}, memberId: {}", requestId, member.getId());
		} else if (ItemRequestStatus.REJECTED.equals(status)) {
			request.reject(member);
			request.getPurchase().buy();
			log.info("🚫[아이템 사용 거절] - requestId: {}, memberId: {}", requestId, member.getId());
		} else {
			log.error("❗[잘못된 요청 상태] - requestId: {}, status: {}", requestId, status);
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}
		return ItemRequestResponseDto.from(request);
	}
}
