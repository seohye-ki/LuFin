package com.lufin.server.item.service;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.item.dto.ItemRequestResponseDto;
import com.lufin.server.member.domain.Member;

public interface ItemRequestService {
	ItemRequestResponseDto requestItemUse(Integer purchaseId, Member student, Integer classroomId);
}
