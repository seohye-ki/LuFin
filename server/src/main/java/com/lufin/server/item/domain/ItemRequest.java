package com.lufin.server.item.domain;

import java.time.LocalDateTime;

import com.lufin.server.member.domain.Member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "item_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_request_id", nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requester_id", nullable = false)
	private Member requester;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private Member approvedBy;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ItemRequestStatus status = ItemRequestStatus.PENDING;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	protected void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();

		if (this.status == null) {
			this.status = ItemRequestStatus.PENDING;
		}
	}

	@PreUpdate
	protected void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	public void approve(Member approver) {
		this.status = ItemRequestStatus.APPROVED;
		this.approvedBy = approver;
	}

	public void reject(Member approver) {
		this.status = ItemRequestStatus.REJECTED;
		this.approvedBy = approver;
	}
}
