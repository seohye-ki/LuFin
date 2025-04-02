package com.lufin.server.credit.domain;

import java.time.LocalDateTime;

import com.lufin.server.member.domain.Member;

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
	@Column(name = "member_id")
	private Integer memberId;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_creditscore_member"))
	private Member member;

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


	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public CreditScoreHistory applyChange(int delta, CreditEventType eventType) {
		int newScore = Math.max(0, Math.min(100, this.score + delta));
		this.score = (byte) newScore;
		this.grade = CreditGrade.fromScore(newScore);
		this.creditStatus = (byte) (newScore <= 30 ? 1 : 0);

		return CreditScoreHistory.of(this.member, (byte) delta, eventType.getDisplayName());
	}

	private CreditScore(Member member) {
		this.member = member;
		this.score = 60;
		this.grade = CreditGrade.fromScore(score);
		this.creditStatus = (byte)0;
	}

	public static CreditScore init(Member member) {
		return new CreditScore(member);
	}

	@PreUpdate
	private void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	public void updateCreditStatusDescription(String description) {
		this.creditStatusDescription = description;
	}
}
