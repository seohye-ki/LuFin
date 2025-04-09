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
	 * 특정 주식 상품에 대해 지정된 시간 범위 내에 생성된 가격 변동이 존재하는지 확인
	 * COUNT 대신 EXISTS 사용으로 성능 향상
	 */
	@Query(value = "SELECT EXISTS(SELECT 1 FROM stock_price_histories sph "
		+ "WHERE sph.stock_product_id = :stockProductId "
		+ "AND sph.created_at BETWEEN :startTime AND :endTime)",
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
		"SELECT sph.stock_price_id, sph.stock_product_id, sph.unit_price, sph.created_at, sph.updated_at  FROM stock_price_histories sph "
			+ "WHERE sph.stock_product_id = :stockProductId "
			+ "ORDER BY sph.created_at "
			+ "LIMIT :count",
		nativeQuery = true)
	List<StockPriceHistory> findLatestPriceByStockProductIdAndCount(
		Integer stockProductId,
		Integer count
	);
}
