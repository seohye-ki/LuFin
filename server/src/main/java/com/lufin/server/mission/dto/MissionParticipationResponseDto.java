package com.lufin.server.mission.dto;

import com.lufin.server.credit.domain.CreditGrade;
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
		String name,
		String profileImage,
		CreditGrade creditGrade,
		MissionParticipationStatus status
	) {
		@QueryProjection
		public MissionParticipationSummaryResponseDto {
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
