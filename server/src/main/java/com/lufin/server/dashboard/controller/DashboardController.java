package com.lufin.server.dashboard.controller;

import static com.lufin.server.common.utils.ValidationUtils.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.annotation.StudentOnly;
import com.lufin.server.common.annotation.TeacherOnly;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.dashboard.dto.StudentDashboardDto;
import com.lufin.server.dashboard.dto.TeacherDashboardDto;
import com.lufin.server.dashboard.service.ClassMembershipValidator;
import com.lufin.server.dashboard.usecase.StudentDashboardUseCase;
import com.lufin.server.dashboard.usecase.TeacherDashboardUseCase;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lufin/dashboards")
public class DashboardController {

	private final StudentDashboardUseCase student;
	private final TeacherDashboardUseCase teacher;
	private final ClassMembershipValidator classMembershipValidator;

	// 학생 - 현재 반 대시보드
	@StudentOnly
	@GetMapping("/my")
	public ResponseEntity<ApiResponse<StudentDashboardDto>> getMyDashboard(HttpServletRequest request) {
		Integer classId = (Integer)request.getAttribute("classId");
		validateClassId(classId);
		Member member = UserContext.get();
		log.info("[학생 대시보드 조회] classId: {}, memberId: {}", classId, member.getId());
		StudentDashboardDto result = student.getDashboard(member.getId(), classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 학생 - 과거 반 대시보드
	@StudentOnly
	@GetMapping(value = "/my", params = "classId")
	public ResponseEntity<ApiResponse<StudentDashboardDto>> getMyPastDashboard(@RequestParam Integer classId) {
		Member member = UserContext.get();
		log.info("[학생 과거 대시보드 조회] classId: {}, memberId: {}", classId, member.getId());
		classMembershipValidator.validateStudentInClass(member.getId(), classId);
		StudentDashboardDto result = student.getDashboard(member.getId(), classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 교사 - 현재 반 대시보드
	@TeacherOnly
	@GetMapping("/my-class")
	public ResponseEntity<ApiResponse<TeacherDashboardDto>> getClassDashboard(HttpServletRequest request) {
		Integer classId = (Integer)request.getAttribute("classId");
		validateClassId(classId);
		Member member = UserContext.get();
		log.info("[교사 대시보드 조회] classId: {}, memberId: {}", classId, member.getId());
		TeacherDashboardDto result = teacher.getDashboard(classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	// 교사 - 학생 대시보드 조회 (현재 반)
	@TeacherOnly
	@GetMapping("/my-class/{userId}")
	public ResponseEntity<ApiResponse<StudentDashboardDto>> getStudentDashboard(HttpServletRequest request,
		@PathVariable Integer userId) {
		Integer classId = (Integer)request.getAttribute("classId");
		validateClassId(classId);
		Member teacher = UserContext.get();
		log.info("[학생 대시보드 조회] classId: {}, teacherId: {}, studentId: {}", classId, teacher.getId(), userId);
		classMembershipValidator.validateTeacherAccessToStudent(teacher.getId(), userId, classId);
		StudentDashboardDto result = student.getDashboard(userId, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}
}
