package com.lufin.server.item.controller;

import static com.lufin.server.common.utils.ValidationUtils.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.annotation.StudentOnly;
import com.lufin.server.common.annotation.TeacherOnly;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.item.dto.ItemRequestApprovalDto;
import com.lufin.server.item.dto.ItemRequestResponseDto;
import com.lufin.server.item.service.ItemRequestService;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lufin/items")
public class ItemRequestController {
	private static final String CLASS_ID = "classId";

	private final ItemRequestService itemRequestService;

	// 아이템 사용
	@StudentOnly
	@PostMapping("/purchases/{purchaseId}/request")
	public ResponseEntity<ApiResponse<ItemRequestResponseDto>> requestItemUse(HttpServletRequest httpRequest,
		@PathVariable Integer purchaseId) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		Member member = UserContext.get();
		ItemRequestResponseDto result = itemRequestService.requestItemUse(purchaseId, member, classId);
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
	@TeacherOnly
	@PatchMapping("/requests/{requestId}")
	public ResponseEntity<ApiResponse<ItemRequestResponseDto>> updateItemRequestStatus(HttpServletRequest httpRequest,
		@PathVariable Integer requestId,
		@RequestBody @Valid ItemRequestApprovalDto requestDto) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		Member member = UserContext.get();
		ItemRequestResponseDto result = itemRequestService.updateItemRequestStatus(requestId, requestDto, member, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}
}
