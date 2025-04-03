package com.lufin.server.dashboard.controller;

import static com.lufin.server.common.utils.ValidationUtils.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.dashboard.dto.StudentDashboardDto;
import com.lufin.server.dashboard.usecase.StudentDashboardUseCase;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lufin/dashboard")
public class DashboardController {

	private final StudentDashboardUseCase dashboardUseCase;

	// 학생
	@GetMapping("/my")
	public ResponseEntity<ApiResponse<StudentDashboardDto>> getDashboard(HttpServletRequest request) {
		Integer classId = (Integer)request.getAttribute("classId");
		validateClassId(classId);
		Member member = UserContext.get();
		log.info("[학생 대시보드 조회] memberId: {}", member.getId());
		StudentDashboardDto result = dashboardUseCase.getDashboard(member.getId(), classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}
}
