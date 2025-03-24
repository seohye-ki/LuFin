package com.lufin.server.mission.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<?> getMissions() {
		//TODO: ApiResponse 활용하는 걸로 변경
		return ResponseEntity.ok().build();
	}

	/**
	 * 미션 상세 조회
	 * @param missionId
	 * @return {}
	 */
	@GetMapping("/{missionId}")
	public ResponseEntity<?> getMissionByMissionId(@PathVariable int missionId) {
		//TODO: ApiResponse 활용하는 걸로 변경
		return ResponseEntity.ok().body(missionId);
	}

	/* 미션 참여 관련 */
}
