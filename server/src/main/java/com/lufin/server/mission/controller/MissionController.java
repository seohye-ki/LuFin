package com.lufin.server.mission.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;
import com.lufin.server.mission.dto.MissionRequestDto;
import com.lufin.server.mission.dto.MissionResponseDto;
import com.lufin.server.mission.service.MissionService;
import com.lufin.server.mission.service.MissionServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/missions")
@RequiredArgsConstructor
public class MissionController {

	/* 미션 관련(참여x) */

	/**
	 * 미션 목록 조회
	 * @return [{}, {} ...]
	 */
	@GetMapping("/")
	public ResponseEntity<?> getMissions(MissionRequestDto request) {
		try {
			Member currentMember = UserContext.get();

			// TODO: classroomID 패러미터에 넣기
			MissionService service = new MissionServiceImpl();

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok().build();
	}

	/**
	 * 미션 상세 조회
	 * @param missionId 미션 고유 번호
	 * @return {}
	 */
	@GetMapping("/{missionId}")
	public ResponseEntity<?> getMissionById(
		MissionResponseDto rqeust,
		@RequestParam(required = true) Integer missionId
	) {
		//TODO: ApiResponse 활용하는 걸로 변경
		return ResponseEntity.ok().body(missionId);
	}

	/* 미션 참여 관련 */
}
