package com.lufin.server.credit.domain;

import java.time.LocalDateTime;

import com.lufin.server.classroom.domain.MemberClassroom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	@JoinColumn(name = "member_class_id", nullable = false,
		foreignKey = @ForeignKey(name = "fk_credithistory_member_classroom"))
	private MemberClassroom memberClassroom;

	@Column(name = "score_change", nullable = false)
	private Byte scoreChange;

	@Column(name = "reason", nullable = false, length = 100)
	private String reason;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	public static CreditScoreHistory of(MemberClassroom memberClassroom, Byte scoreChange, String reason) {
		CreditScoreHistory history = new CreditScoreHistory();
		history.memberClassroom = memberClassroom;
		history.scoreChange = scoreChange;
		history.reason = reason;
		history.createdAt = LocalDateTime.now();
		return history;
	}
}
