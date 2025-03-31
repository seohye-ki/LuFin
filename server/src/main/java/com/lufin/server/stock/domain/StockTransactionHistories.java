package com.lufin.server.stock.domain;

import java.time.LocalDateTime;

import com.lufin.server.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "stock_histories")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockTransactionHistories {
	/* 연관관계 */
	@Setter
	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Setter(AccessLevel.PROTECTED)
	@ManyToOne
	@JoinColumn(name = "stock_product_id", nullable = false)
	private StockProduct stockProduct;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_history_id", nullable = false)
	private int id;

	@PositiveOrZero
	@Column(name = "type", nullable = false)
	private int type;

	@PositiveOrZero
	@Column(name = "quantity", nullable = false)
	private int quantity;

	@PositiveOrZero
	@Column(name = "unit_price", nullable = false)
	private int price;

	@PositiveOrZero
	@Column(name = "total_value", nullable = false)
	private int totalPrice;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	// 시간 설정
	@PrePersist
	public void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = createdAt;
	}

	@PreUpdate
	public void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	/**
	 * 주식 투자 내역 객체를 생성하는 팩토리 메서드
	 */
	public static StockTransactionHistories create(int type,
		int quantity,
		int price,
		int totalPrice,
		StockProduct stockProduct,
		Member member
	) {
		StockTransactionHistories history = StockTransactionHistories.builder()
			.type(type)
			.quantity(quantity)
			.price(price)
			.totalPrice(totalPrice)
			.build();

		stockProduct.addTransactionHistories(history);
		member.addStockTransactionHistories(history);
		return history;
	}

	// 무한 루프 방지를 위해 contains()를 사용하면서 id를 기준으로 체크하기 위해 equals override
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		StockTransactionHistories that = (StockTransactionHistories)o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(id);
	}

}
