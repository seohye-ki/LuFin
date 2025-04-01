package com.lufin.server.classroom.service;

import java.util.List;

import com.lufin.server.classroom.dto.ClassCodeRequest;
import com.lufin.server.classroom.dto.ClassCodeResponse;
import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.FindClassesResponse;
import com.lufin.server.classroom.dto.LoginWithClassResponse;
import com.lufin.server.member.domain.Member;

public interface ClassroomService {

	// [교사] 클래스 생성
	LoginWithClassResponse createClassroom(ClassRequest request, Member teacher);

	// 본인이 속한 모든 클래스 조회
	List<FindClassesResponse> findClasses(int memberId);

	// 현재 속한 클래스 조회
	FindClassesResponse findCurrentClass(int memberId);

	// 클래스 코드 조회
	ClassCodeResponse findClassCode(Member teacher);

	// [학생] 클래스 코드로 클래스 입장
	LoginWithClassResponse enrollClass(Member member, ClassCodeRequest request);
}
