package com.lufin.server.mission.dto;

import com.lufin.server.mission.domain.MissionParticipationStatus;
import com.querydsl.core.annotations.QueryProjection;

public class MissionParticipationResponseDto {
	public record MissionApplicationResponseDto(
		Integer participationId
	) {
		@QueryProjection
		public MissionApplicationResponseDto {
		}

		/**
		 * MissionParticipation 엔티티를 응답 DTO로 변환
		 */
		public static MissionApplicationResponseDto missionParticipationToMissionApplicationResponseDto(
			Integer participationId) {
			return new MissionApplicationResponseDto(participationId);
		}
	}

	public record MissionParticipationSummaryResponseDto(
		Integer participationId,
		String name,
		String profileImage,
		MissionParticipationStatus status
	) {
		@QueryProjection
		public MissionParticipationSummaryResponseDto {
		}

		/**
		 * MissionParticipation 엔티티를 dto로 변환
		 */
		public static MissionParticipationSummaryResponseDto missionParticipationToMissionParticipationSummaryResponseDto(
			Integer participationId,
			String name,
			String profileImage,
			MissionParticipationStatus status
		) {
			return new MissionParticipationSummaryResponseDto(participationId, name, profileImage, status);
		}
	}

	public record MissionParticipationStatusResponseDto(
		MissionParticipationStatus status
	) {
		@QueryProjection
		public MissionParticipationStatusResponseDto {
		}

		/**
		 * MissionParticipation 엔티티를 DTO로 변환
		 */
		public static MissionParticipationStatusResponseDto missionParticipationToMissionParticipationStatusResponseDto(
			MissionParticipationStatus status) {
			return new MissionParticipationStatusResponseDto(status);
		}
	}
}
