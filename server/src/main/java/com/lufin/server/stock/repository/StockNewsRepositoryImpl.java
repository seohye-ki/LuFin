package com.lufin.server.stock.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.QStockNews;
import com.lufin.server.stock.domain.QStockProduct;
import com.lufin.server.stock.dto.QStockNewsResponseDto_NewsInfoDto;
import com.lufin.server.stock.dto.StockNewsResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockNewsRepositoryImpl implements StockNewsRepository {
	private final JPAQueryFactory queryFactory;

	/**
	 * 툭정 주식 공시 정보 목록 조회
	 * @param stockProductId
	 */
	@Override
	public List<StockNewsResponseDto.NewsInfoDto> getAllNews(
		Integer stockProductId
	) {
		QStockNews stockNews = QStockNews.stockNews;
		QStockProduct stockProduct = QStockProduct.stockProduct;

		List<StockNewsResponseDto.NewsInfoDto> result = queryFactory
			.select(new QStockNewsResponseDto_NewsInfoDto(
				stockNews.id,
				stockNews.content,
				stockNews.createdAt,
				stockNews.updatedAt
			))
			.from(stockNews)
			.where(stockProduct.id.eq(stockProductId))
			.fetch();

		return result;
	}

	/**
	 * 특정 주식 공시 정보 상세 조회
	 * @param stockProductId
	 * @param newsId
	 */
	@Override
	public StockNewsResponseDto.NewsInfoDto getNewsByNewsId(
		Integer stockProductId,
		Integer newsId
	) {
		QStockProduct stockProduct = QStockProduct.stockProduct;
		QStockNews stockNews = QStockNews.stockNews;

		BooleanBuilder builder = new BooleanBuilder();

		builder.and(stockProduct.id.eq(stockProductId));
		builder.and(stockNews.id.eq(newsId));

		StockNewsResponseDto.NewsInfoDto result = queryFactory
			.select(new QStockNewsResponseDto_NewsInfoDto(
				stockNews.id,
				stockNews.content,
				stockNews.createdAt,
				stockNews.updatedAt
			))
			.from(stockNews)
			.where(builder)
			.fetchOne();

		return result;
	}
}
