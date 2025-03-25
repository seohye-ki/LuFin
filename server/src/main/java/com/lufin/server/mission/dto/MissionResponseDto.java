package com.lufin.server.mission.dto;

import java.time.LocalDateTime;

import com.lufin.server.mission.domain.Mission;
import com.lufin.server.mission.domain.MissionStatus;
import com.querydsl.core.annotations.QueryProjection;

public class MissionResponseDto {
	// TODO: record를 활용해 dto 작성

	/**
	 * 미션 상세 응답 DTO
	 */
	public record MissionDetailResponseDto(
		Integer missionId,
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
		@QueryProjection
		public MissionDetailResponseDto {
		}

		/**
		 * Mission 엔티티를 응답 DTO로 변환
		 */
		public static MissionDetailResponseDto missionEntityToMissionDetailResponseDto(Mission mission) {
			return new MissionDetailResponseDto(
				mission.getMissionId(),
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
		Integer missionId,
		String title,
		Integer difficulty,
		Integer maxParticipants,
		Integer currentParticipants,
		Integer wage,
		LocalDateTime missionDate,
		MissionStatus status
	) {

		// querydsl 객체 생성을 위한 생성자
		@QueryProjection
		public MissionSummaryResponseDto {
		}

		/**
		 * Mission 엔티티를 요약 DTO로 변환
		 */
		public static MissionSummaryResponseDto missionEntityToMissionDetailResponseDto(Mission mission) {
			return new MissionSummaryResponseDto(
				mission.getMissionId(),
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
