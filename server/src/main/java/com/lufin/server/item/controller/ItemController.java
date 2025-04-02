package com.lufin.server.item.controller;

import static com.lufin.server.common.utils.ValidationUtils.*;

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

import com.lufin.server.common.annotation.TeacherOnly;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.item.dto.ItemDto;
import com.lufin.server.item.dto.ItemRequestApprovalDto;
import com.lufin.server.item.dto.ItemRequestResponseDto;
import com.lufin.server.item.dto.ItemResponseDto;
import com.lufin.server.item.service.ItemRequestService;
import com.lufin.server.item.service.ItemService;
import com.lufin.server.member.domain.Member;
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
	private final ItemRequestService itemRequestService;

	// 아이템 생성
	@TeacherOnly
	@PostMapping
	public ResponseEntity<ApiResponse<ItemResponseDto>> createItem(HttpServletRequest httpRequest,
		@RequestBody @Valid ItemDto request) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		ItemResponseDto result = itemService.createItem(request, classId);
		return ResponseEntity.status(201).body(ApiResponse.success(result));
	}

	// 아이템 전체 조회
	@GetMapping
	public ResponseEntity<ApiResponse<List<ItemResponseDto>>> getItems(HttpServletRequest httpRequest) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		Member member = UserContext.get();
		List<ItemResponseDto> result = itemService.getItems(member, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 단일 조회
	@TeacherOnly
	@GetMapping("/{itemId}")
	public ResponseEntity<ApiResponse<ItemResponseDto>> getItemDetail(HttpServletRequest httpRequest,
		@PathVariable Integer itemId) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		ItemResponseDto result = itemService.getItemDetail(itemId, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 수정
	@TeacherOnly
	@PutMapping("/{itemId}")
	public ResponseEntity<ApiResponse<ItemResponseDto>> updateItem(HttpServletRequest httpRequest,
		@PathVariable Integer itemId, @RequestBody @Valid ItemDto request) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		ItemResponseDto result = itemService.updateItem(itemId, request, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 삭제
	@TeacherOnly
	@DeleteMapping("/{itemId}")
	public ResponseEntity<Void> deleteItem(HttpServletRequest httpRequest,
		@PathVariable Integer itemId) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		itemService.deleteItem(itemId, classId);
		return ResponseEntity.noContent().build();
	}

	// 아이템 사용
	@PostMapping("/purchases/{purchaseId}/request")
	public ResponseEntity<ApiResponse<ItemRequestResponseDto>> requestItemUse(HttpServletRequest httpRequest,
		@PathVariable Integer purchaseId) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		ItemRequestResponseDto result = itemRequestService.requestItemUse(purchaseId, UserContext.get(), classId);
		return ResponseEntity.status(201).body(ApiResponse.success(result));
	}

	// 아이템 사용 목록 조회
	@GetMapping("/requests")
	public ResponseEntity<ApiResponse<List<ItemRequestResponseDto>>> getItemRequests(HttpServletRequest httpRequest) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		List<ItemRequestResponseDto> result = itemRequestService.getItemRequests(classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 사용요청 승인 또는 거절
	@PatchMapping("/requests/{requestId}")
	public ResponseEntity<ApiResponse<ItemRequestResponseDto>> updateItemRequestStatus(HttpServletRequest httpRequest,
		@PathVariable Integer requestId,
		@RequestBody @Valid ItemRequestApprovalDto requestDto) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		ItemRequestResponseDto result = itemRequestService.updateItemRequestStatus(requestId, requestDto, UserContext.get(), classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}
}
