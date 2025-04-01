package com.lufin.server.stock.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.QStockProduct;
import com.lufin.server.stock.dto.QStockResponseDto_StockInfoDto;
import com.lufin.server.stock.dto.StockResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockProductRepositoryCustomImpl implements StockProductRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	/**
	 * 주식 상품 전체 조회
	 */
	@Override
	public List<StockResponseDto.StockInfoDto> getAllStocks() {
		QStockProduct stockProduct = QStockProduct.stockProduct;

		List<StockResponseDto.StockInfoDto> result = queryFactory
			.select(new QStockResponseDto_StockInfoDto(
				stockProduct.id,
				stockProduct.name,
				stockProduct.description,
				stockProduct.initialPrice,
				stockProduct.currentPrice,
				stockProduct.createdAt,
				stockProduct.updatedAt
			))
			.from(stockProduct)
			.fetch();

		return result;
	}

	@Override
	public StockResponseDto.StockInfoDto getStock(Integer stockProductId) {
		QStockProduct stockProduct = QStockProduct.stockProduct;

		BooleanBuilder builder = new BooleanBuilder();

		builder.and(stockProduct.id.eq(stockProductId));

		StockResponseDto.StockInfoDto result = queryFactory
			.select(new QStockResponseDto_StockInfoDto(
				stockProduct.id,
				stockProduct.name,
				stockProduct.description,
				stockProduct.initialPrice,
				stockProduct.currentPrice,
				stockProduct.createdAt,
				stockProduct.updatedAt
			))
			.from(stockProduct)
			.where(builder)
			.fetchOne();

		return result;
	}
}
