package com.lufin.server.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.StockPortfolio;

@Repository
public interface StockPortfolioRepository extends JpaRepository<StockPortfolio, Integer> {
	// 특정 유저의 특정 상품 포트폴리오 조회
	StockPortfolio findByStockProductIdAndMemberId(Integer stockProductId, Integer memberId);
}
