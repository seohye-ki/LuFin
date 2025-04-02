package com.lufin.server.credit.domain;

import java.time.LocalDateTime;

import com.lufin.server.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credit_score_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditScoreHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_credithistory_member"))
	private Member member;

	@Column(name = "score_change", nullable = false)
	private Byte scoreChange;

	@Column(name = "reason", nullable = false, length = 100)
	private String reason;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	private CreditScoreHistory(Member member, Byte scoreChange, String reason) {
		this.member = member;
		this.scoreChange = scoreChange;
		this.reason = reason;
	}

	public static CreditScoreHistory of(Member member, Byte scoreChange, String reason) {
		return new CreditScoreHistory(member, scoreChange, reason);
	}

	@PrePersist
	private void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
	}

}
