package com.lufin.server.item.domain;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_request_id", nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_purchase_id", nullable = false)
	private ItemPurchase purchase;

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

	public static ItemRequest create(ItemPurchase purchase, Member requester) {
		ItemRequest request = new ItemRequest();
		request.purchase = purchase;
		request.requester = requester;
		request.status = ItemRequestStatus.PENDING;
		request.createdAt = LocalDateTime.now();
		request.updatedAt = LocalDateTime.now();
		return request;
	}

	public void approve(Member approver) {
		this.status = ItemRequestStatus.APPROVED;
		this.approvedBy = approver;
	}

	public void reject(Member approver) {
		this.status = ItemRequestStatus.REJECTED;
		this.approvedBy = approver;
	}

	@PreUpdate
	protected void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}
}
