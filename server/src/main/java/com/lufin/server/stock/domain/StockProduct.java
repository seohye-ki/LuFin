package com.lufin.server.stock.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
	@Builder.Default
	@OneToMany(mappedBy = "stockProduct", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StockPortfolio> portfolios = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "stockProduct", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StockNews> news = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "stockProduct", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StockPriceHistory> priceHistory = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "stockProduct", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StockTransactionHistory> transactionHistories = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_product_id", nullable = false)
	private Integer id;

	@Column(name = "name", nullable = false)
	@Max(value = 50)
	@NotBlank
	private String name;

	@Column(name = "description", nullable = false)
	@NotBlank
	private String description;

	@PositiveOrZero
	@NotNull
	@Column(name = "initial_price", nullable = false)
	private Integer initialPrice;

	@PositiveOrZero
	@NotNull
	@Column(name = "current_price", nullable = false)
	private Integer currentPrice;

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

	/* 연관관계 메서드 */

	/**
	 * 주식 공시 정보(StockNews)와의 양방향 연관관계를 위한 메서드
	 * @param news
	 */
	public void addNews(StockNews news) {
		// 중복 체크 후 없으면 추가
		if (!this.news.contains(news)) {
			this.news.add(news);
		}

		// 양방향 관계 설정 (무한 루프 방지 조건)
		if (news.getStockProduct() != this) {
			news.setStockProduct(this);
		}
	}

	/**
	 * 주식 가격 기록(StockPriceHistory)와의 양방향 연관관계를 위한 메서드
	 * @param priceHistory
	 */
	public void addPriceHistory(StockPriceHistory priceHistory) {
		if (!this.priceHistory.contains(priceHistory)) {
			this.priceHistory.add(priceHistory);
		}

		// 양방향 관계 설정 (무한 루프 방지 조건)
		if (priceHistory.getStockProduct() != this) {
			priceHistory.setStockProduct(this);
		}
	}

	/**
	 * 주식 포트폴리오(StockPortfolio)와의 양방향 연관관계를 위한 메서드
	 * @param portfolio
	 */
	public void addPortfolio(StockPortfolio portfolio) {
		if (!this.portfolios.contains(portfolio)) {
			this.portfolios.add(portfolio);
		}

		// 양방향 관계 설정 (무한 루프 방지 조건)
		if (portfolio.getStockProduct() != this) {
			portfolio.setStockProduct(this);
		}
	}

	/**
	 * 주식 투자 내역(StockTransactionHistories)와의 양방향 연관관계를 위한 메서드
	 * @param transactionHistories
	 * @return
	 */
	public void addTransactionHistories(StockTransactionHistory transactionHistories) {
		if (!this.transactionHistories.contains(transactionHistories)) {
			this.transactionHistories.add(transactionHistories);
		}

		// 양방향 관계 설정 (무한 루프 방지 조건)
		if (transactionHistories.getStockProduct() != this) {
			transactionHistories.setStockProduct(this);
		}
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

		StockProduct that = (StockProduct)o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(id);
	}

}
