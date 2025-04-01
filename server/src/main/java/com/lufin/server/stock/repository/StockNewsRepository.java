package com.lufin.server.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.stock.domain.StockNews;

public interface StockNewsRepository extends JpaRepository<StockNews, Integer>, StockNewsRepositoryCustom {
}
