package com.lufin.server.mission.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lufin.server.classroom.domain.Classroom;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "mission")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission {
	public static final int MAX_TITLE_LENGTH = 100;

	// JPA 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", nullable = false)
	private Classroom classroom;

	@Builder.Default
	@OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true) // 양방향 연관관계 주인 mission으로 설정
	private List<MissionParticipation> participations = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true) // 양방향 연관관계 주인 mission으로 설정
	private List<MissionImage> images = new ArrayList<>();

	// Member Field
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	@Column(name = "mission_id")
	private Integer id;

	@Column(name = "class_id", insertable = false, updatable = false, nullable = false)
	private Integer classId;

	@Column(name = "title", nullable = false, length = MAX_TITLE_LENGTH)
	@NotBlank
	private String title;

	@Column(name = "content", nullable = false)
	@NotBlank
	private String content;

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

	public void modifyTitle(String title) {
		this.title = title;
	}

	public void modifyContent(String content) {
		this.content = content;
	}

	public void modifyDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	public void modifyMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public void modifyWage(Integer wage) {
		this.wage = wage;
	}

	public void modifyMissionDate(LocalDateTime missionDate) {
		this.missionDate = missionDate;
	}

	/**
	 * 미션 객체를 생성하는 팩토리 메서드
	 */
	public static Mission create(Integer classId, Classroom classroom, String title, String content, Integer difficulty,
		Integer maxParticipants, Integer wage, LocalDateTime missionDate) {
		Mission mission = Mission.builder()
			.classId(classId)
			.title(title)
			.content(content)
			.difficulty(difficulty)
			.maxParticipants(maxParticipants)
			.wage(wage)
			.missionDate(missionDate)
			.build();

		// 교실과 연관관계 설정
		classroom.addMission(mission);

		return mission;
	}

	/* participation 관계 유지를 위한 메서드 */

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

	/**
	 * missionParticipation과 양방향 연관관계를 위한 메서드
	 * 내부적 연관관계 설정에서만 사용하므로 participation이 null이 아니어야함
	 * @param participation
	 */
	public void addParticipation(MissionParticipation participation) {
		if (this.currentParticipants >= this.maxParticipants) {
			//TODO: error 코드 변경
			throw new IllegalStateException("Maximum participants reached");
		}

		// 중복 체크 후 없으면 추가
		if (!participations.contains(participation)) {
			participations.add(participation);
			this.incrementParticipants();
		}

		// 양방향 관계 설정 (무한 루프 방지 조건)
		if (participation.getMission() != this) {
			participation.setMission(this);
		}

	}

	/**
	 * missionParticipation과 양방향 연관관계를 위한 메서드
	 * 내부적 연관관계 설정에서만 사용하므로 participation이 null이 아니어야함
	 * @param participation
	 */
	void removeParticipation(MissionParticipation participation) {
		if (participations.isEmpty()) {
			throw new IllegalStateException("No participation found");
		}

		if (!participations.contains(participation)) {
			throw new IllegalStateException("No such participation found");
		}

		boolean removed = participations.remove(participation);

		if (removed) {
			this.decrementParticipants();
		}
	}

	/* image 관계 유지를 위한 메서드 */

	/**
	 * missionImage와 mission 간의 양방향 관계의 일관성을 유지하기 위한 메서드
	 * 연관관계 관리용으로, 반드시 null이 아닌 missionImage만 전달되어야 합니다.
	 */
	public void addImage(MissionImage missionImage) {
		// 중복 체크 후 없으면 추가
		if (!images.contains(missionImage)) {
			images.add(missionImage);
		}

		if (missionImage.getMission() != this) {
			missionImage.setMission(this);
		}

	}

	void removeImage(MissionImage missionImage) {
		if (images.isEmpty()) {
			throw new IllegalStateException("No mission image found");
		}

		if (!images.contains(missionImage)) {
			throw new IllegalStateException("No such mission image found");
		}

		images.remove(missionImage);
	}

	/**
	 * 현재 참가자 수와 참가자 목록의 크기가 일치하는지 확인
	 */
	public boolean isParticipantsCountConsistent() {
		return this.currentParticipants == this.participations.size();
	}

	/* classroom 연관 관계 메서드 */
	public void setClassroom(Classroom classroom) {
		if (classroom == null) {
			throw new IllegalArgumentException("Classroom cannot be null");
		}

		this.classroom = classroom;
	}

	// 무한 루프 방지를 위해 contains()를 사용하면서 id를 기준으로 체크하기 위해 equals override
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Mission mission = (Mission)o;
		return id != null && id.equals(mission.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
