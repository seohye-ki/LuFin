package com.lufin.server.stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.StockProduct;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

@Repository
public interface StockProductRepository
	extends JpaRepository<StockProduct, Integer>, StockProductRepositoryCustom {

	/**
	 * 주식 상품 ID로 조회하면서 비관적 락(쓰기 락)을 적용합니다.
	 * 주식 가격이나 수량 변경 시 동시성 문제를 방지합니다.
	 * @param id 주식 상품 ID
	 * @return 비관적 락이 적용된 주식 상품 객체
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT s FROM StockProduct s WHERE s.id = :id")
	Optional<StockProduct> findByIdWithPessimisticLock(@Param("id") Integer id);

}
