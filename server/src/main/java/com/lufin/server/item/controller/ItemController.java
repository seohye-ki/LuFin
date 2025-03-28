package com.lufin.server.item.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.item.dto.ItemDto;
import com.lufin.server.item.dto.ItemPurchaseRequestDto;
import com.lufin.server.item.dto.ItemPurchaseResponseDto;
import com.lufin.server.item.dto.ItemRequestApprovalDto;
import com.lufin.server.item.dto.ItemRequestResponseDto;
import com.lufin.server.item.dto.ItemResponseDto;
import com.lufin.server.item.service.ItemPurchaseService;
import com.lufin.server.item.service.ItemRequestService;
import com.lufin.server.item.service.ItemService;

import com.lufin.server.member.support.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lufin/items")
public class ItemController {
	private static final String CLASS_ID = "classId";

	private final ItemService itemService;
	private final ItemPurchaseService itemPurchaseService;
	private final ItemRequestService itemRequestService;

	// TODO: 선생님 AOP넣기 (@TeacherOnly)

	// 아이템 생성
	@PostMapping
	public ResponseEntity<ApiResponse<ItemResponseDto>> createItem(@RequestBody @Valid ItemDto request) {
		ItemResponseDto result = itemService.createItem(request, UserContext.get());
		return ResponseEntity.status(201).body(ApiResponse.success(result));
	}

	// 아이템 전체 조회
	@GetMapping
	public ResponseEntity<ApiResponse<List<ItemResponseDto>>> getItems() {
		List<ItemResponseDto> result = itemService.getItems(UserContext.get());
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 단일 조회
	@GetMapping("/{itemId}")
	ResponseEntity<ApiResponse<ItemResponseDto>> getItemDetail(@PathVariable Integer itemId) {
		ItemResponseDto result = itemService.getItemDetail(itemId, UserContext.get());
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 수정
	@PutMapping("/{itemId}")
	public ResponseEntity<ApiResponse<ItemResponseDto>> updateItem(@PathVariable Integer itemId,
		@RequestBody @Valid ItemDto request) {
		ItemResponseDto result = itemService.updateItem(itemId, request, UserContext.get());
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 삭제
	@DeleteMapping("/{itemId}")
	public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Integer itemId) {
		itemService.deleteItem(itemId, UserContext.get());
		return ResponseEntity.noContent().build();
	}

	// 아이템 구매
	@PostMapping("/purchase")
	public ResponseEntity<ApiResponse<List<ItemPurchaseResponseDto>>> purchaseItem(@RequestBody @Valid ItemPurchaseRequestDto request) {
		List<ItemPurchaseResponseDto> result = itemPurchaseService.purchaseItem(request, UserContext.get());
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 나의 구매 아이템 조회
	@GetMapping("/inventory")
	public ResponseEntity<ApiResponse<List<ItemPurchaseResponseDto>>> getInventory() {
		List<ItemPurchaseResponseDto> result = itemPurchaseService.getInventory(UserContext.get());
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 특정 아이템의 구매 내역 조회
	@GetMapping("/{itemId}/purchases")
	public ResponseEntity<ApiResponse<List<ItemPurchaseResponseDto>>> getItemPurchaseHistory(@PathVariable Integer itemId) {
		List<ItemPurchaseResponseDto> result = itemPurchaseService.getItemPurchaseHistory(itemId, UserContext.get());
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 환불
	@PatchMapping("purchases/{purchaseId}/refund")
	ResponseEntity<ApiResponse<ItemPurchaseResponseDto>> refundItem(@PathVariable Integer purchaseId) {
		ItemPurchaseResponseDto result = itemPurchaseService.refundItem(purchaseId, UserContext.get());
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 사용
	@PostMapping("/purchases/{purchaseId}/request")
	public ResponseEntity<ApiResponse<ItemRequestResponseDto>> requestItemUse(HttpServletRequest httpRequest, @PathVariable Integer purchaseId) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		ItemRequestResponseDto result = itemRequestService.requestItemUse(purchaseId, UserContext.get(), classId);
		return ResponseEntity.status(201).body(ApiResponse.success(result));
	}

	// 아이템 사용 목록 조회
	@GetMapping("/requests")
	public ResponseEntity<ApiResponse<List<ItemRequestResponseDto>>> getItemRequests(HttpServletRequest httpRequest) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		List<ItemRequestResponseDto> result = itemRequestService.getItemRequests(classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 사용요청 승인,거절
	@PatchMapping("/requests/{requestId}")
	public ResponseEntity<ApiResponse<ItemRequestResponseDto>> updateItemRequestStatus(HttpServletRequest httpRequest, @PathVariable Integer requestId, @RequestBody @Valid ItemRequestApprovalDto requestDto) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		ItemRequestResponseDto result = itemRequestService.updateItemRequestStatus(requestId, requestDto, UserContext.get(), classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}
}
