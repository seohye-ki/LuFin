package com.lufin.server.stock.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.StockPriceHistory;

@Repository
public interface StockPriceHistoryRepository extends JpaRepository<StockPriceHistory, Integer> {
	/**
	 * 특정 주식 상품에 대해 지정된 시간 범위 내에 생성된 뉴스가 존재하는지 확인
	 * COUNT 대신 EXISTS 사용으로 성능 향상
	 */
	@Query(value = "SELECT EXISTS(SELECT 1 FROM stock_histories sh "
		+ "WHERE sh.stock_product_id = :stockProductId "
		+ "AND sh.created_at BETWEEN :startTime AND :endTime)",
		nativeQuery = true)
	Integer existsByStockProductIdAndCreatedAtBetween(
		Integer stockProductId,
		LocalDateTime startTime,
		LocalDateTime endTime
	);

	/**
	 * 특정 주식 상품에 대해 가격 변동 내역을 count 개수 만큼 최신순으로 가져옴
	 */
	@Query(value =
		"SELECT sh.stock_history_id, sh.stock_product_id, sh.unit_price, sh.created_at  FROM stock_histories sh "
			+ "WHERE sh.stock_product_id = :stockProductId "
			+ "ORDER BY sh.created_at DESC "
			+ "LIMIT :count",
		nativeQuery = true)
	List<StockPriceHistory> findLatestPriceByStockProductIdAndCount(
		Integer stockProductId,
		Integer count
	);
}
