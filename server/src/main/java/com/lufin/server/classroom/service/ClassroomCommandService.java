package com.lufin.server.classroom.service;

import com.lufin.server.classroom.dto.ClassCodeRequest;
import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.classroom.dto.LoginWithClassResponse;
import com.lufin.server.classroom.dto.UpdateClassRequest;
import com.lufin.server.member.domain.Member;

public interface ClassroomCommandService {

	// [교사] 클래스 생성
	LoginWithClassResponse createClassroom(ClassRequest request, Member teacher);

	// [학생] 클래스 코드로 클래스 입장
	LoginWithClassResponse enrollClass(Member member, ClassCodeRequest request);

	// [교사] 클래스 수정
	ClassResponse updateClassroom(Member member, UpdateClassRequest request);

	// [교사] 클래스 삭제
	void deleteClassroom(Member member, int classId);

	// [교사] 클래스 소속 변경
	LoginWithClassResponse changeClassroom(Member member, int classId);
}
