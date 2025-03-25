package com.lufin.server.mission.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "missions")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission {
	public static final int MAX_TITLE_LENGTH = 100;

	// JPA 연관관계 매핑
	@Builder.Default
	@OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true) // 양방향 연관관계 주인 mission으로 설정
	private List<MissionParticipation> participations = new ArrayList<>();

	// Member Field
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	@Column(name = "mission_id")
	private Integer id;

	@Column(name = "class_id", nullable = false)
	private Integer classId;

	@Column(name = "title", nullable = false, length = MAX_TITLE_LENGTH)
	@NotBlank
	private String title;

	@Column(name = "content", nullable = false)
	@NotBlank
	private String content;

	@Column(name = "image", length = 255)
	private String image;

	@Column(name = "difficulty", nullable = false)
	@Min(1)
	@Max(3)
	@Builder.Default
	private Integer difficulty = 2;

	@Column(name = "max_participants", nullable = false)
	private Integer maxParticipants;

	@Column(name = "current_participants", nullable = false)
	@Builder.Default
	private Integer currentParticipants = 0;

	@Column(name = "wage", nullable = false)
	private Integer wage;

	@Column(name = "mission_date", nullable = false)
	private LocalDateTime missionDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@Builder.Default
	private MissionStatus status = MissionStatus.RECRUITING;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// 시간 설정
	@PrePersist
	private void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
	}

	@PreUpdate
	private void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	// 로직
	public void updateStatus(MissionStatus status) {
		if (status == null) {
			//TODO: error code 변경 필요
			throw new IllegalArgumentException("Statsu cannot be null");
		}
		this.status = status;
	}

	public void incrementParticipants() {
		if (this.currentParticipants >= this.maxParticipants) {
			//TODO: error 코드 변경
			throw new IllegalStateException("Cannot exceed maximum participants: " + this.maxParticipants);
		}
		this.currentParticipants++;
	}

	public void decrementParticipants() {
		if (this.currentParticipants <= 0) {
			//TODO: error 코드 변경
			throw new IllegalStateException("Current participants cannot be negative");
		}
		this.currentParticipants--;
	}

	public void addParticipation(MissionParticipation participation) {
		if (participation == null) {
			//TODO: error 코드 변경
			throw new IllegalArgumentException("Participation cannot be null");
		}

		if (this.currentParticipants >= this.maxParticipants) {
			//TODO: error 코드 변경
			throw new IllegalStateException("Maximum participants reached");
		}

		participations.add(participation);
		this.incrementParticipants();
	}

	public void removeParticipation(MissionParticipation participation) {
		if (participation == null) {
			//TODO: error 코드 변경
			throw new IllegalArgumentException("Participation cannot be null");
		}

		boolean removed = participations.remove(participation);
		if (removed) {
			this.decrementParticipants();
		}
	}

	/**
	 * 이미지가 없는 미션 객체를 생성하는 팩토리 메서드
	 */
	public static Mission createWithoutImage(Integer classId, String title, String content, Integer difficulty,
		Integer maxParticipants, Integer wage, LocalDateTime missionDate) {
		return Mission.builder()
			.classId(classId)
			.title(title)
			.content(content)
			.difficulty(difficulty)
			.maxParticipants(maxParticipants)
			.wage(wage)
			.missionDate(missionDate)
			.build();
	}

	/**
	 * 이미지가 있는 미션 객체를 생성하는 팩토리 메서드
	 */
	public static Mission createWithImage(Integer classId, String title, String content, String image,
		Integer difficulty,
		Integer maxParticipants, Integer wage, LocalDateTime missionDate) {
		return Mission.builder()
			.classId(classId)
			.title(title)
			.content(content)
			.image(image)
			.difficulty(difficulty)
			.maxParticipants(maxParticipants)
			.wage(wage)
			.missionDate(missionDate)
			.build();
	}

	/**
	 * 현재 참가자 수와 참가자 목록의 크기가 일치하는지 확인
	 */
	public boolean isParticipantsCountConsistent() {
		return this.currentParticipants == this.participations.size();
	}

	// 기본 메서드는 DTO에서
}
