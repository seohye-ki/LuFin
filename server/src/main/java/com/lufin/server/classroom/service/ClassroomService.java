package com.lufin.server.classroom.service;

import java.util.List;

import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.classroom.dto.FindClassesResponse;
import com.lufin.server.member.domain.Member;

public interface ClassroomService {

	// 클래스 생성
	ClassResponse createClassroom(ClassRequest request, Member teacher);

	// 본인이 속한 모든 클래스 조회
	List<FindClassesResponse> findClasses(int memberId);

	// 현재 속한 클래스 조회
	FindClassesResponse findCurrentClass(int memberId);
}
