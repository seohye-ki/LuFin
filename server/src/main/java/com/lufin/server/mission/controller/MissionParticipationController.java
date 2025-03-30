package com.lufin.server.mission.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.common.utils.ValidationUtils;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;
import com.lufin.server.mission.dto.MissionParticipationResponseDto;
import com.lufin.server.mission.service.MissionParticipationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/missions/{missionId}/participations")
@RequiredArgsConstructor
public class MissionParticipationController {
	private final MissionParticipationService missionParticipationService;

	/* 미션 참여 관련 */

	/**
	 * 미션 참여 신청
	 * @param missionId
	 * @param request
	 * @return
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<MissionParticipationResponseDto.MissionApplicationResponseDto>> applyMission(
		@PathVariable @Positive Integer missionId,
		HttpServletRequest request
	) {
		Integer classId = (Integer)request.getAttribute("classId");
		ValidationUtils.validateClassId(classId);

		Member currentMember = UserContext.get();

		MissionParticipationResponseDto.MissionApplicationResponseDto response = missionParticipationService.applyMission(
			classId,
			missionId,
			currentMember);

		return ResponseEntity.status(201).body(ApiResponse.success(response));
	}

	/**
	 * 미션 참여자 목록 전체 조회
	 * @param missionId
	 * @param request
	 * @return
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<List<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto>>> getAllMissionParticipants(
		@PathVariable @Positive Integer missionId,
		HttpServletRequest request
	) {
		Integer classId = (Integer)request.getAttribute("classId");
		ValidationUtils.validateClassId(classId);

		Member currentMember = UserContext.get();

		List<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto> participants = missionParticipationService.getAllMissionParticipants(
			classId,
			missionId,
			currentMember
		);

		return ResponseEntity.status(200).body(ApiResponse.success(participants));
	}
}
