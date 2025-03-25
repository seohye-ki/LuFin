package com.lufin.server.mission.dto;

import java.time.LocalDateTime;

import com.lufin.server.mission.domain.Mission;
import com.lufin.server.mission.domain.MissionStatus;

public class MissionResponseDto {
	// TODO: record를 활용해 dto 작성

	/**
	 * 미션 상세 응답 DTO
	 */
	public record MissionDetailResponseDto(
		Integer id,
		Integer classId,
		String title,
		String content,
		String image,
		Integer difficulty,
		Integer maxParticipants,
		Integer currentParticipants,
		Integer wage,
		LocalDateTime missionDate,
		MissionStatus status,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		/**
		 * Mission 엔티티를 응답 DTO로 변환
		 */
		public static MissionDetailResponseDto missionEntityToMissionDetailResponseDto(Mission mission) {
			return new MissionDetailResponseDto(
				mission.getId(),
				mission.getClassId(),
				mission.getTitle(),
				mission.getContent(),
				mission.getImage(),
				mission.getDifficulty(),
				mission.getMaxParticipants(),
				mission.getCurrentParticipants(),
				mission.getWage(),
				mission.getMissionDate(),
				mission.getStatus(),
				mission.getCreatedAt(),
				mission.getUpdatedAt()
			);
		}
	}

	/**
	 * 미션 목록 응답 DTO (요약본)
	 */
	public record MissionSummaryResponseDto(
		Integer id,
		String title,
		Integer difficulty,
		Integer maxParticipants,
		Integer currentParticipants,
		Integer wage,
		LocalDateTime missionDate,
		MissionStatus status
	) {
		/**
		 * Mission 엔티티를 요약 DTO로 변환
		 */
		public static MissionSummaryResponseDto fromEntity(Mission mission) {
			return new MissionSummaryResponseDto(
				mission.getId(),
				mission.getTitle(),
				mission.getDifficulty(),
				mission.getMaxParticipants(),
				mission.getCurrentParticipants(),
				mission.getWage(),
				mission.getMissionDate(),
				mission.getStatus()
			);
		}
	}

}
