package com.lufin.server.classroom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.ClassResponse;
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
}
