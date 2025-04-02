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
@Table(name = "item_purchases")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

	@Column(name = "purchase_price", nullable = false)
	private Integer purchasePrice;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ItemPurchaseStatus status = ItemPurchaseStatus.BUY;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public static ItemPurchase create(Item item, Member member, int itemCount) {
		ItemPurchase purchase = new ItemPurchase();
		purchase.item = item;
		purchase.member = member;
		purchase.itemCount = itemCount;
		purchase.purchasePrice = item.getPrice();
		purchase.status = ItemPurchaseStatus.BUY;
		purchase.createdAt = LocalDateTime.now();
		purchase.updatedAt = LocalDateTime.now();
		return purchase;
	}

	public void buy() {
		this.status = ItemPurchaseStatus.BUY;
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

	public void pending() {
		this.status = ItemPurchaseStatus.PENDING;
	}

	public boolean isPurchasedBy(Member member) {
		return this.member.getId().equals(member.getId());
	}

	@PreUpdate
	protected void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}
}
