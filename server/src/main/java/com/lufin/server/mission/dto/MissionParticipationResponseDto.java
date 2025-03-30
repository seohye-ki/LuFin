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
		String name,
		String profileImage,
		Integer creditRating,
		Enum<MissionParticipationStatus> status
	) {
		@QueryProjection
		public MissionParticipationSummaryResponseDto {
		}
	}
}
