package com.lufin.server.classroom.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.classroom.dto.FindClassesResponse;
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

	// 클래스 생성
	@PostMapping
	ResponseEntity<ApiResponse<ClassResponse>> createClassroom(@RequestBody @Valid ClassRequest classRequest) {
		Member currentMember = UserContext.get();
		ClassResponse result = classroomService.createClassroom(classRequest, currentMember);
		return ResponseEntity.status(201).body(ApiResponse.success(result));
	}

	// 본인이 소속된 모든 클래스 조회
	@GetMapping
	ResponseEntity<ApiResponse<List<FindClassesResponse>>> getAllClassrooms() {
		Member currentMember = UserContext.get();
		List<FindClassesResponse> result = classroomService.findClasses(currentMember.getId());
		return ResponseEntity.status(200).body(ApiResponse.success(result));
	}

	@GetMapping("/current")
	ResponseEntity<ApiResponse<FindClassesResponse>> getCurrentClassroom() {
		Member currentMember = UserContext.get();
		FindClassesResponse response = classroomService.findCurrentClass(currentMember.getId());
		return ResponseEntity.status(200).body(ApiResponse.success(response));
	}
}
