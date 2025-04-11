package com.lufin.server.mission.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lufin.server.mission.domain.MissionParticipationStatus;

public record MyMissionDto(
	int missionId,                          // 미션 ID
	int participationId,                    // 학생이 이 미션에 참여한 참여 ID
	String title,                           // 미션 제목
	MissionParticipationStatus status,      // 미션 상태 (미션 수행 중, 검토 중, 완료, 실패, 거절됨)
	int wage,                               // 미션 보상 금액
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime missionDate               // 미션 날짜
) {
}
