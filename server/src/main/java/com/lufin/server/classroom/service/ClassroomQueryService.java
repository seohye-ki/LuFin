package com.lufin.server.classroom.service;

import java.util.List;

import com.lufin.server.classroom.dto.ClassCodeResponse;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.member.domain.Member;

public interface ClassroomQueryService {

	// 본인이 속한 모든 클래스 조회
	List<ClassResponse> findClasses(int memberId);

	// 현재 속한 클래스 조회
	ClassResponse findCurrentClass(int memberId);

	// 클래스 코드 조회
	ClassCodeResponse findClassCode(Member teacher);
}
