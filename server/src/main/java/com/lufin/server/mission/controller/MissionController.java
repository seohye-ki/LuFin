package com.lufin.server.mission.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.member.support.UserContext;
import com.lufin.server.mission.dto.MissionRequestDto;
import com.lufin.server.mission.dto.MissionResponseDto;
import com.lufin.server.mission.service.MissionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/missions")
@RequiredArgsConstructor
public class MissionController {
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
	 * @return "data":
	 * {missionId, title, content, image, difficulty, maxParticipants, currentParticipants, wage, missionDate}
	 */
	@GetMapping("/{missionId}")
	public ResponseEntity<ApiResponse<MissionResponseDto.MissionDetailResponseDto>> getMissionById(
		@PathVariable @Positive Integer missionId,
		HttpServletRequest request
	) {
		Integer classId = request.getAttribute("classId") != null ? (Integer)request.getAttribute("classId") : null;

		Member currentMember = UserContext.get();
		Enum<MemberRole> role = currentMember.getMemberRole();

		MissionResponseDto.MissionDetailResponseDto mission = missionService.getMissionById(
			classId,
			missionId,
			role);

		return ResponseEntity.status(200).body(ApiResponse.success(mission));

	}

	/**
	 * 미션 생성
	 * @param requestDto 미션 생성에 필요한 데이터
	 * @param request 리퀘스트 그 자체(JSON -> JAVA 객체로 바꿔줌)
	 * @param bindingResult DTO 검증 결과
	 * @return "data": { "id": Number }
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<MissionResponseDto.MissionPostResponseDto>> createMission(
		@Valid @RequestBody MissionRequestDto.MissionRequestInfoDto requestDto,
		HttpServletRequest request,
		BindingResult bindingResult) {
		// 유효성 검증에 실패한 경우 직접 처리
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(400).body(ApiResponse.failure(ErrorCode.INVALID_INPUT_VALUE));
		}

		Integer classId = request.getAttribute("classId") != null ? (Integer)request.getAttribute("classId") : null;

		MissionResponseDto.MissionPostResponseDto response = missionService.postMission(requestDto, classId);

		return ResponseEntity.status(201).body(ApiResponse.success(response));
	}

	@DeleteMapping("/{missionId}")
	public ResponseEntity<ApiResponse> deleteMission(
		@PathVariable @Positive Integer missionId,
		HttpServletRequest request
	) {
		Integer classId = request.getAttribute("classId") != null ? (Integer)request.getAttribute("classId") : null;

		Member currentMember = UserContext.get();
		Enum<MemberRole> role = currentMember.getMemberRole();

		missionService.deleteMission(classId, missionId, role);
		return ResponseEntity.noContent().build();

	}

	@PutMapping("/{missionId}")
	public ResponseEntity<ApiResponse<MissionResponseDto.MissionDetailResponseDto>> modifyMission(
		@PathVariable @Positive Integer missionId,
		@Valid @RequestBody MissionRequestDto.MissionRequestInfoDto requestDto,
		HttpServletRequest request,
		BindingResult bindingResult
	) {

		// TODO: null에서 data로 변경
		return ResponseEntity.status(200).body(ApiResponse.success(null));
	}

	/* 미션 참여 관련 */
}
