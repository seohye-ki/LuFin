package com.lufin.server.stock.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.StockNews;

@Repository
public interface StockNewsRepository extends JpaRepository<StockNews, Integer>, StockNewsRepositoryCustom {
	// 공시 정보 중복 여부 체크

	/**
	 * 특정 주식 상품에 대해 지정된 시간 범위 내에 생성된 뉴스가 존재하는지 확인
	 * COUNT 대신 EXISTS 사용으로 성능 향상
	 */
	@Query(value = "SELECT EXISTS(SELECT 1 FROM stock_news sn "
		+ "WHERE sn.stock_product_id = :stockProductId "
		+ "AND sn.created_at BETWEEN :startTime AND :endTime)",
		nativeQuery = true)
	Integer existsByStockProductIdAndCreatedAtBetween(
		Integer stockProductId,
		LocalDateTime startTime,
		LocalDateTime endTime
	);

}
