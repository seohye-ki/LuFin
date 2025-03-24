package com.lufin.server.mission.domain;

import java.time.LocalDateTime;
import java.util.Objects;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "mission_participation")
@Builder // 생성자 생성 어노테이션
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MissionParticipation {
	// JPA 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_id", nullable = false) // name을 통해 JPA가 자동으로 외래 키 관리
	private Mission mission;

	// Member Field
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "participation_id")
	private Integer id;

	@Column(name = "member_id", nullable = false)
	private Integer memberId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@Builder.Default
	private MissionParticipationStatus status = MissionParticipationStatus.IN_PROGRESS;

	@Column(name = "wage_status", nullable = false)
	@Builder.Default
	private Boolean wageStatus = false;

	@Column(name = "reject_count", nullable = false)
	@Builder.Default
	private Integer rejectCount = 0;

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

	/**
	 * 참여 상태를 업데이트
	 *
	 * @param status 새로운 참여 상태 (null이 아니어야 함)
	 * @throws IllegalArgumentException 상태가 null인 경우
	 */
	public void updateStatus(MissionParticipationStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("Status cannot be null");
		}
		this.status = status;
	}

	/**
	 * 임금 지급 상태를 지급됨으로 표시
	 */
	public void markWagePaid() {
		this.wageStatus = true;
	}

	/**
	 * 거절 횟수를 증가
	 */
	public void incrementRejectCount() {
		this.rejectCount++;
	}

	/**
	 * 미션과의 연관관계를 설정
	 * 이 메서드는 양방향 연관관계의 일관성을 유지하기 위해 사용
	 *
	 * @param mission 연관될 미션 (null이 아니어야 함)
	 * @throws IllegalArgumentException 미션이 null인 경우
	 */
	public void setMission(Mission mission) {
		if (mission == null) {
			// TODO: error 코드 수정
			throw new IllegalArgumentException("Mission cannot be null");
		}
		this.mission = mission;
	}

	/**
	 * 새로운 미션 참여를 생성
	 * 이 팩토리 메서드는 양방향 연관관계의 일관성을 유지
	 *
	 * @param mission  참여할 미션 (null이 아니어야 함)
	 * @param memberId 참여 회원 ID
	 * @return 생성된 미션 참여 객체
	 * @throws IllegalArgumentException 미션이 null이거나 memberId가 null인 경우
	 */
	public static MissionParticipation create(Mission mission, Integer memberId) {
		if (mission == null) {
			// TODO: error 코드 수정
			throw new IllegalArgumentException("Mission cannot be null");
		}
		if (memberId == null) {
			// TODO: error 코드 수정
			throw new IllegalArgumentException("Member ID cannot be null");
		}

		MissionParticipation participation = MissionParticipation.builder()
			.mission(mission)
			.memberId(memberId)
			.build();

		mission.addParticipation(participation);
		return participation;
	}

	/**
	 * 현재 미션이 성공 상태인지 확인
	 *
	 * @return 성공 상태인 경우 true
	 */
	public boolean isCompleted() {
		return this.status == MissionParticipationStatus.SUCCESS;
	}

	/* 기본 메서드 */

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MissionParticipation that = (MissionParticipation)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "MissionParticipation{"
			+
			"id=" + id
			+
			", memberId=" + memberId
			+

			", status=" + status
			+
			", wageStatus=" + wageStatus
			+
			", rejectCount=" + rejectCount
			+
			'}';
	}
}