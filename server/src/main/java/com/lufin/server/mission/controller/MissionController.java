package com.lufin.server.mission.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.common.utils.TokenUtils;
import com.lufin.server.mission.dto.MissionResponseDto;
import com.lufin.server.mission.service.MissionService;

import io.jsonwebtoken.Claims;
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
	public ResponseEntity<?> getMissions(
		@RequestHeader("Authorization") String token
	) {
		Claims jwtClaim = tokenUtils.extractClaims(token);
		Integer classId = jwtClaim.get("classId", Integer.class);

		List<MissionResponseDto.MissionSummaryResponseDto> missions = missionService.getAllMissions(classId);
		return ResponseEntity.status(200).body(ApiResponse.success(missions));
	}

	/**
	 * 미션 상세 조회
	 *
	 * @param missionId 미션 고유 번호
	 * @return {}
	 */
	@GetMapping("/{missionId}")
	public ResponseEntity<?> getMissionById(
		@PathVariable Integer missionId,
		@RequestHeader("Authorization") String token
	) {

		Claims jwtClaim = tokenUtils.extractClaims(token);
		Integer classId = jwtClaim.get("classId", Integer.class);
		String role = jwtClaim.get("role", String.class);

		MissionResponseDto.MissionDetailResponseDto mission = missionService.getMissionById(
			classId,
			missionId,
			role);

		return ResponseEntity.ok().body(ApiResponse.success(mission));

	}

	/* 미션 참여 관련 */
}
