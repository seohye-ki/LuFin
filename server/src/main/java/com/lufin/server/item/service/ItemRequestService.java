package com.lufin.server.item.service;

import java.util.List;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.item.dto.ItemRequestApprovalDto;
import com.lufin.server.item.dto.ItemRequestResponseDto;
import com.lufin.server.member.domain.Member;

import jakarta.validation.Valid;

public interface ItemRequestService {
	ItemRequestResponseDto requestItemUse(Integer purchaseId, Member student, Integer classroomId);

	List<ItemRequestResponseDto> getItemRequests(Integer classroomId);

	ItemRequestResponseDto updateItemRequestStatus(Integer requestId, @Valid ItemRequestApprovalDto requestDto, Member member, Integer classId);
}
