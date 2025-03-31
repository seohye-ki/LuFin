package com.lufin.server.stock.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "stock_price_histories")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockPriceHistory {

	@Setter(AccessLevel.PROTECTED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_product_id", nullable = false)
	private StockProduct stockProduct;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_price_id", nullable = false)
	private int id;

	@Column(name = "unit_price", nullable = false)
	private int unitPrice;

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
	 * 가격 변동 기록 객체를 생성하는 팩토리 메서드
	 */
	public static StockPriceHistory create(int unitPrice, StockProduct stockProduct) {
		StockPriceHistory history = StockPriceHistory.builder()
			.unitPrice(unitPrice)
			.build();

		stockProduct.addPriceHistory(history);
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

		StockPriceHistory history = (StockPriceHistory)o;
		return id == history.id;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(id);
	}

}
