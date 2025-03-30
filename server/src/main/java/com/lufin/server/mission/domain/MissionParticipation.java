package com.lufin.server.mission.domain;

import java.time.LocalDateTime;

import com.lufin.server.member.domain.Member;

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
@Table(name = "mission_participations")
@Builder // 생성자 생성 어노테이션
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MissionParticipation {
	// JPA 연관관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_id", nullable = false) // name을 통해 JPA가 자동으로 외래 키 관리
	private Mission mission;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	// Member Field
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "participation_id")
	private Integer participationId;

	@Column(name = "member_id", insertable = false, updatable = false, nullable = false)
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
	 * 새로운 미션 참여를 생성
	 * 양방향 연관관계의 일관성을 유지하기 위한 로직이 추가되어 있음
	 * @param mission  참여할 미션
	 * @param memberId 참여 회원 ID
	 * @return 생성된 미션 참여 객체
	 */
	public static MissionParticipation create(Mission mission, Member member, Integer memberId) {
		// service 레이어에서 null 체크 실행

		// 객체를 먼저 생성하고 메서드를 통해 양방향 관계 설정
		MissionParticipation participation = MissionParticipation.builder()
			.memberId(memberId)
			.build();

		mission.addParticipation(participation);
		member.addMissionParticipation(participation);

		return participation;
	}

	/**
	 * 미션과의 연관관계를 설정 (package-private)
	 * 이 메서드는 양방향 연관관계의 일관성을 유지하기 위해 사용
	 * @param mission 연관될 미션 (null이 아니어야 함)
	 */
	void setMission(Mission mission) {
		// 중복체크는 addParticipation에서 진행
		this.mission = mission;
	}

	/**
	 * 회원과의 연관관계를 설정 (package-private)
	 * 이 메소드는 양방향 연관관계의 일관성을 유지하기 위해 사용
	 * @param member 연관될 회원 (null이 될 수 있음)
	 */
	public void setMember(Member member) {
		// 기존 회원과 연관관계 제거
		if (this.member != null && this.member != member) {
			this.member.getMissionParticipations().remove(this);
		}

		this.member = member;
		this.memberId = member != null ? member.getId() : null;

		// 새로운 회원과 연관관계 설정
		if (member != null && !member.getMissionParticipations().contains(this)) {
			member.getMissionParticipations().add(this);
		}
	}

	/**
	 * 현재 미션이 성공 상태인지 확인
	 * @return 성공 상태인 경우 true
	 */
	public boolean isCompleted() {
		return this.status == MissionParticipationStatus.SUCCESS;
	}

	// 무한 루프 방지를 위해 contains()를 사용하면서 id를 기준으로 체크하기 위해 equals override
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MissionParticipation that = (MissionParticipation)o;
		return participationId != null && participationId.equals(that.participationId);
	}

	@Override
	public int hashCode() {
		return participationId != null ? participationId.hashCode() : 0;
	}
}
