package com.lufin.server.stock.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "stock_products")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockProduct {
	//TODO: 다른 테이블과 연관관계 설정

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_product_id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	@Max(value = 50)
	@NotBlank
	private String name;

	@Column(name = "description", nullable = false)
	@NotBlank
	private String description;

	@Column(name = "initial_price", nullable = false)
	private int initialPrice;

	@Column(name = "current_price", nullable = false)
	private int currentPrice;

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
	 * 주식 상품 객체를 생성하는 팩토리 메서드
	 */
	public static StockProduct create(String name, String description, int initialPrice, int currentPrice) {
		return StockProduct.builder()
			.name(name)
			.description(description)
			.initialPrice(initialPrice)
			.currentPrice(currentPrice)
			.build();
	}

}
