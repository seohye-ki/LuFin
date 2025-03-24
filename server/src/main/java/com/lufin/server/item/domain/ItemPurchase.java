package com.lufin.server.item.domain;

import java.time.LocalDateTime;

import com.lufin.server.member.domain.Member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "item_purchase")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemPurchase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_purchase_id", nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "item_count", nullable = false)
	private Integer itemCount = 1;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ItemPurchaseStatus status = ItemPurchaseStatus.BUY;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	protected void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();

		if (this.itemCount == null) {
			this.itemCount = 1;
		}

		if (this.status == null) {
			this.status = ItemPurchaseStatus.BUY;
		}
	}

	@PreUpdate
	protected void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	public void used() {
		this.status = ItemPurchaseStatus.USED;
	}

	public void refund() {
		this.status = ItemPurchaseStatus.REFUND;
	}

	public void expired() {
		this.status = ItemPurchaseStatus.EXPIRED;
	}

	public boolean isPurchasedBy(Member member) {
		return this.member.equals(member);
	}
}
