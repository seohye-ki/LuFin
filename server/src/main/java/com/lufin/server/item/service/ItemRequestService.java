package com.lufin.server.item.service;

import java.util.List;
import java.util.Optional;

import com.lufin.server.item.domain.ItemRequestStatus;
import com.lufin.server.item.dto.ItemRequestApprovalDto;
import com.lufin.server.item.dto.ItemRequestResponseDto;
import com.lufin.server.member.domain.Member;

import jakarta.validation.Valid;

public interface ItemRequestService {
	ItemRequestResponseDto requestItemUse(Integer purchaseId, Member student, Integer classroomId);

	List<ItemRequestResponseDto> getItemRequests(Integer classroomId);

	ItemRequestResponseDto updateItemRequestStatus(Integer requestId, @Valid ItemRequestApprovalDto requestDto,
		Member member, Integer classId);

	// 해당 학생이 반(classId)에서 요청한 아이템 중 가장 최근 요청을 조회
	Optional<ItemRequestStatus> getLatestItemRequestStatus(int memberId, int classId);
}
