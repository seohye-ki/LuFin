package com.lufin.server.mission.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.common.utils.TokenUtils;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;
import com.lufin.server.mission.dto.MissionResponseDto;
import com.lufin.server.mission.service.MissionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/missions")
@RequiredArgsConstructor
public class MissionController {
	private final TokenUtils tokenUtils;
	private final MissionService missionService;

	/* 미션 관련(참여x) */

	/**
	 * 미션 목록 조회
	 * @return [{}, {} ...]
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<List<MissionResponseDto.MissionSummaryResponseDto>>> getMissions(
		HttpServletRequest request
	) {
		Integer classId = request.getAttribute("classId") != null ? (Integer)request.getAttribute("classId") : null;

		List<MissionResponseDto.MissionSummaryResponseDto> missions = missionService.getAllMissions(classId);
		return ResponseEntity.status(200).body(ApiResponse.success(missions));
	}

	/**
	 * 미션 상세 조회
	 * @param missionId 미션 고유 번호
	 * @return "data": {missionId, title, content, image, difficulty, maxParticipants, currentParticipants, wage, missionDate}
	 */
	@GetMapping("/{missionId}")
	public ResponseEntity<ApiResponse<MissionResponseDto.MissionDetailResponseDto>> getMissionById(
		@PathVariable @Positive Integer missionId,
		HttpServletRequest request
	) {
		Integer classId = request.getAttribute("classId") != null ? (Integer)request.getAttribute("classId") : null;

		Member currentMember = UserContext.get();
		String role = String.valueOf(currentMember.getMemberRole());

		MissionResponseDto.MissionDetailResponseDto mission = missionService.getMissionById(
			classId,
			missionId,
			role);

		return ResponseEntity.ok().body(ApiResponse.success(mission));

	}

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createMission() {
		
	}

	/* 미션 참여 관련 */
}
