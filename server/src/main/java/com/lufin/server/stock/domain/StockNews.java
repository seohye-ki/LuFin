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

@Getter
@Entity
@Table(name = "stock_news")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockNews {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_product_id", nullable = false)
	private StockProduct stockProduct;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_news_id", nullable = false)
	private int id;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	// 시간 설정
	@PrePersist
	private void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
	}

	@PreUpdate
	private void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	public void modifyContent(String content) {
		this.content = content;
	}

	/**
	 * 주식 공시 정보 객체를 생성하는 팩토리 메서드
	 */
	public static StockNews create(StockProduct product, String content) {
		StockNews news = StockNews.builder()
			.content(content)
			.build();

		// 주식 상품과의 연관관계 설정
		product.addNews(news);

		return news;
	}

	/* 연관관계 메서드 */

	/**
	 * 주식 상품(StockProduct)와의 양방향 연관관계를 위한 메서드
	 * @param stockProduct
	 */
	void setStockProduct(StockProduct stockProduct) {
		// 중복 체크는 StockProduct.addNews()에서 진행
		this.stockProduct = stockProduct;
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

		StockNews stockNews = (StockNews)o;
		return id == stockNews.id;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(id);
	}

}
