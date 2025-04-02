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
import com.lufin.server.item.dto.ItemPurchaseRequestDto;
import com.lufin.server.item.dto.ItemPurchaseResponseDto;
import com.lufin.server.item.service.ItemPurchaseService;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lufin/items")
public class ItemPurchaseController {
	private static final String CLASS_ID = "classId";

	private final ItemPurchaseService itemPurchaseService;

	// 아이템 구매
	@StudentOnly
	@PostMapping("/purchase")
	public ResponseEntity<ApiResponse<List<ItemPurchaseResponseDto>>> purchaseItem(HttpServletRequest httpRequest, @RequestBody @Valid ItemPurchaseRequestDto request) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		Member member = UserContext.get();
		List<ItemPurchaseResponseDto> result = itemPurchaseService.purchaseItem(request, member, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 나의 구매 아이템 조회
	@StudentOnly
	@GetMapping("/inventory")
	public ResponseEntity<ApiResponse<List<ItemPurchaseResponseDto>>> getInventory(HttpServletRequest httpRequest) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		Member member = UserContext.get();
		List<ItemPurchaseResponseDto> result = itemPurchaseService.getInventory(member, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 특정 아이템의 구매 내역 조회
	@TeacherOnly
	@GetMapping("/{itemId}/purchases")
	public ResponseEntity<ApiResponse<List<ItemPurchaseResponseDto>>> getItemPurchaseHistory(HttpServletRequest httpRequest,
		@PathVariable Integer itemId) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		List<ItemPurchaseResponseDto> result = itemPurchaseService.getItemPurchaseHistory(itemId, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 아이템 환불
	@StudentOnly
	@PatchMapping("purchases/{purchaseId}/refund")
	ResponseEntity<ApiResponse<ItemPurchaseResponseDto>> refundItem(HttpServletRequest httpRequest,
		@PathVariable Integer purchaseId) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		Member member = UserContext.get();
		ItemPurchaseResponseDto result = itemPurchaseService.refundItem(purchaseId, member, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

}
