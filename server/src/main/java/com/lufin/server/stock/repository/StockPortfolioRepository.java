package com.lufin.server.stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.StockPortfolio;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

@Repository
public interface StockPortfolioRepository extends JpaRepository<StockPortfolio, Integer> {
	// 특정 유저의 특정 상품 포트폴리오 조회

	/**
	 * 회원 ID와 주식 상품 ID로 포트폴리오를 조회하면서 비관적 락을 적용합니다.
	 * 주식 매수/매도 시 동시에 같은 포트폴리오에 접근하는 것을 방지합니다.
	 *
	 * @param stockProductId 주식 상품 ID
	 * @param memberId 회원 ID
	 * @return 비관적 락이 적용된 주식 포트폴리오 객체
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT sp FROM StockPortfolio sp WHERE sp.stockProduct.id = :stockProductId AND sp.member.id = :memberId")
	Optional<StockPortfolio> findByStockProductIdAndMemberIdWithPessimisticLock(
		@Param("stockProductId") Integer stockProductId,
		@Param("memberId") Integer memberId);
}

