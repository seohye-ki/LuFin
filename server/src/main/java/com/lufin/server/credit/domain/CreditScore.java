package com.lufin.server.credit.domain;

import java.time.LocalDateTime;

import com.lufin.server.classroom.domain.MemberClassroom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credit_scores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditScore {

	@Id
	@Column(name = "member_class_id")
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "member_class_id", foreignKey = @ForeignKey(name = "fk_credit_score_member_classroom"))
	private MemberClassroom memberClassroom;

	@Column(name = "score", nullable = false)
	private Byte score;

	@Enumerated(EnumType.STRING)
	@Column(name = "grade", nullable = false)
	private CreditGrade grade;

	/**
	 * 신용 상태
	 * 0: 정상, 1: 신용 불량자
	 */
	@Column(name = "credit_status", nullable = false)
	private Byte creditStatus;

	@Column(name = "credit_status_description", columnDefinition = "TEXT")
	private String creditStatusDescription;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	private CreditScore(MemberClassroom memberClassroom) {
		this.memberClassroom = memberClassroom;
		this.score = 60;
		this.grade = CreditGrade.fromScore(60);
		this.creditStatus = 0;
	}

	public static CreditScore init(MemberClassroom memberClassroom) {
		return new CreditScore(memberClassroom);
	}

	public CreditScoreHistory applyChange(int delta, CreditEventType eventType) {
		int newScore = Math.max(0, Math.min(100, this.score + delta));
		this.score = (byte)newScore;
		this.grade = CreditGrade.fromScore(newScore);
		this.creditStatus = (byte)(newScore <= 30 ? 1 : 0);

		return CreditScoreHistory.of(this.memberClassroom, (byte)delta, eventType.getDisplayName());
	}

	@PrePersist
	protected void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	public void updateCreditStatusDescription(String description) {
		this.creditStatusDescription = description;
	}
}
