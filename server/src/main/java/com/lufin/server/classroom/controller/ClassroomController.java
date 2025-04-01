package com.lufin.server.classroom.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.classroom.dto.ClassCodeResponse;
import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.classroom.dto.DeleteClassRequest;
import com.lufin.server.classroom.dto.FindClassesResponse;
import com.lufin.server.classroom.dto.LoginWithClassResponse;
import com.lufin.server.classroom.dto.UpdateClassRequest;
import com.lufin.server.classroom.service.ClassroomService;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/classes")
@RequiredArgsConstructor
public class ClassroomController {

	private final ClassroomService classroomService;

	// [교사] 클래스 생성
	@PostMapping
	ResponseEntity<ApiResponse<LoginWithClassResponse>> createClassroom(@RequestBody @Valid ClassRequest classRequest) {
		Member currentMember = UserContext.get();
		LoginWithClassResponse result = classroomService.createClassroom(classRequest, currentMember);
		return ResponseEntity.status(201).body(ApiResponse.success(result));
	}

	// 본인이 소속된 모든 클래스 조회
	@GetMapping
	ResponseEntity<ApiResponse<List<FindClassesResponse>>> getAllClassrooms() {
		Member currentMember = UserContext.get();
		List<FindClassesResponse> result = classroomService.findClasses(currentMember.getId());
		return ResponseEntity.status(200).body(ApiResponse.success(result));
	}

	// 	본인이 현재 소속된 클래스 조회
	@GetMapping("/current")
	ResponseEntity<ApiResponse<FindClassesResponse>> getCurrentClassroom() {
		Member currentMember = UserContext.get();
		FindClassesResponse response = classroomService.findCurrentClass(currentMember.getId());
		return ResponseEntity.status(200).body(ApiResponse.success(response));
	}

	// [교사] 클래스 코드 공유
	@GetMapping("/current/code")
	ResponseEntity<ApiResponse<ClassCodeResponse>> getClassroomCode() {
		Member currentMember = UserContext.get();
		ClassCodeResponse response = classroomService.findClassCode(currentMember);
		return ResponseEntity.status(200).body(ApiResponse.success(response));
	}

	// [교사] 클래스 수정
	@PatchMapping("/current")
	ResponseEntity<ApiResponse<ClassResponse>> updateClassroom(@RequestBody @Valid UpdateClassRequest request) {
		Member currentMember = UserContext.get();
		ClassResponse response = classroomService.updateClassroom(currentMember, request);
		return ResponseEntity.status(200).body(ApiResponse.success(response));
	}

	// [교사] 클래스 삭제
	@DeleteMapping
	ResponseEntity<ApiResponse<Void>> deleteClassroom(@RequestBody @Valid DeleteClassRequest request) {
		Member currentMember = UserContext.get();
		classroomService.deleteClassroom(currentMember, request.classId());
		return ResponseEntity.status(204).build();
	}

}
