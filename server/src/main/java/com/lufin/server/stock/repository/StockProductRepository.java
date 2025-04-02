package com.lufin.server.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.StockProduct;

@Repository
public interface StockProductRepository
	extends JpaRepository<StockProduct, Integer>, StockProductRepositoryCustom {
}
