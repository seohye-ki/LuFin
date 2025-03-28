package com.lufin.server.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import lombok.Getter;

@Getter
@Embeddable
public class MemberStatus {

	@Column(name = "credit_rating", nullable = false)
	private Integer creditRating;

	/**
	 * 신용 상태
	 * 0: 정상, 1: 신용 불량자
	 */
	@Column(name = "credit_status", nullable = false)
	private Byte creditStatus;

	@Column(name = "credit_status_description", columnDefinition = "TEXT")
	private String creditStatusDescription;

	@Column(name = "activation_status", nullable = false)
	private byte activationStatus;

	public MemberStatus() {
		this.creditRating = 60;
		this.creditStatus = 0;
		this.creditStatusDescription = null;
	}

	public void updateStatus(Byte status, String description) {
		this.creditStatus = status;
		this.creditStatusDescription = description;
	}

	@PrePersist
	private void setActivationStatus() {
		this.activationStatus = 1;
	}

	// 회원 탈퇴 시 호출
	public void updateActivationStatus() {
		this.activationStatus = 0;
	}
}
