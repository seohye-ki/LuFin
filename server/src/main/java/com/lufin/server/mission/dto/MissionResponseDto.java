package com.lufin.server.mission.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.mission.domain.Mission;
import com.lufin.server.mission.domain.MissionImage;
import com.lufin.server.mission.domain.MissionParticipation;
import com.lufin.server.mission.domain.MissionStatus;
import com.querydsl.core.annotations.QueryProjection;

public class MissionResponseDto {
	/**
	 * Image DTO to avoid entity serialization issues
	 */
	public record MissionImageDto(
		Integer id,
		String objectKey // Include only the necessary fields
	) {
		public static MissionImageDto fromEntity(MissionImage image) {
			return new MissionImageDto(
				image.getMissionImageId(),
				image.getObjectKey() // Assuming this field exists
			);
		}
	}

	/**
	 * Participation DTO to avoid entity serialization issues
	 */
	public record MissionParticipationDto(
		Integer id,
		Integer memberId,
		String memberProfileImage,
		String memberName  // Include only the necessary fields
	) {
		public static MissionParticipationDto fromEntity(MissionParticipation participation) {
			return new MissionParticipationDto(
				participation.getParticipationId(),
				participation.getMember() != null ? participation.getMember().getId() : null,
				participation.getMember() != null ? participation.getMember().getProfileImage() : null,
				participation.getMember() != null ? participation.getMember().getName() : null
			);
		}
	}

	/**
	 * 미션 상세 응답 DTO
	 */
	public record MissionDetailResponseDto(
		Integer missionId,
		Integer classId,
		String title,
		String content,
		List<MissionImageDto> images,  // Changed from List<MissionImage>
		Integer difficulty,
		Integer maxParticipants,
		Integer currentParticipants,
		List<MissionParticipationDto> participations,  // Changed from List<MissionParticipation>
		Integer wage,

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime missionDate,

		MissionStatus status,

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime createdAt,

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
				mission.getId(),
				mission.getClassroom().getId(),
				mission.getTitle(),
				mission.getContent(),
				mission.getImages() != null
					? mission.getImages().stream()
					.map(MissionImageDto::fromEntity)
					.collect(Collectors.toList())
					: List.of(),
				mission.getDifficulty(),
				mission.getMaxParticipants(),
				mission.getCurrentParticipants(),
				mission.getParticipations() != null
					? mission.getParticipations().stream()
					.map(MissionParticipationDto::fromEntity)
					.collect(Collectors.toList())
					: List.of(),
				mission.getWage(),
				mission.getMissionDate(),
				mission.getStatus(),
				mission.getCreatedAt(),
				mission.getUpdatedAt()
			);
		}
	}

	public record MissionResponseWrapperDto(
		List<MissionMyResponseDto> myData,
		List<MissionSummaryResponseDto> allData
	) {

		// 내 미션과 전체 미션 리스트로 wrapper dto 생성
		public static MissionResponseWrapperDto createWrapperDto(
			List<MissionMyResponseDto> myMissions,
			List<MissionSummaryResponseDto> allMissions
		) {
			return new MissionResponseWrapperDto(myMissions, allMissions);
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
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
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

	/**
	 * 미션 목록 응답 DTO (요약본)
	 */
	public record MissionMyResponseDto(
		Integer missionId,
		Integer participationId,
		String title,
		Integer difficulty,
		Integer maxParticipants,
		Integer currentParticipants,
		Integer wage,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime missionDate,
		MissionStatus status
	) {

		// querydsl 객체 생성을 위한 생성자
		@QueryProjection
		public MissionMyResponseDto {
		}

		/**
		 * Mission 엔티티를 요약 DTO로 변환
		 */
		public static MissionMyResponseDto convertEntityToDto(Mission mission, Integer memberId) {
			MissionParticipation myParticipation = mission.getParticipations().stream()
				.filter(p -> p.getMember().getId().equals(memberId))
				.findFirst()
				.orElseThrow(() -> new BusinessException(ErrorCode.MISSION_NOT_FOUND));

			return new MissionMyResponseDto(
				mission.getId(),
				myParticipation.getParticipationId(),
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

	/**
	 * 미션 등록 응답 DTO
	 * @param missionId
	 */
	public record MissionPostResponseDto(Integer missionId) {
		@QueryProjection
		public MissionPostResponseDto {
		}
	}
}