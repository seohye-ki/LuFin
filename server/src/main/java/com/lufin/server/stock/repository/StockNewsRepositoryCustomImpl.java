package com.lufin.server.stock.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.QStockNews;
import com.lufin.server.stock.domain.QStockProduct;
import com.lufin.server.stock.domain.StockNews;
import com.lufin.server.stock.dto.StockNewsResponseDto;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockNewsRepositoryCustomImpl implements StockNewsRepositoryCustom {
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

		List<StockNews> newsEntityList = queryFactory
			.selectFrom(stockNews)
			.leftJoin(stockNews.stockProduct, stockProduct).fetchJoin()
			.where(stockNews.stockProduct.id.eq(stockProductId))
			.fetch();

		if (newsEntityList.isEmpty()) {
			return new ArrayList<>();
		}

		return newsEntityList.stream()
			.map(StockNewsResponseDto.NewsInfoDto::stockNewsEntityToNewsInfoDto)
			.toList();
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

		StockNews newsEntity = queryFactory
			.selectFrom(stockNews)
			.leftJoin(stockNews.stockProduct, stockProduct).fetchJoin()
			.where(stockNews.id.eq(newsId))
			.fetchOne();

		if (newsEntity == null) {
			return null;
		}

		return StockNewsResponseDto.NewsInfoDto.stockNewsEntityToNewsInfoDto(newsEntity);
	}

	/**
	 * 모든 stockProduct에 대한 가장 최근 뉴스 조회
	 */
	@Override
	public List<StockNews> findLatestNewsForAllStockProducts() {
		QStockNews stockNews = QStockNews.stockNews;
		QStockProduct stockProduct = QStockProduct.stockProduct;
		QStockNews subStockNews = new QStockNews("subStockNews");

		// 서브쿼리와 조인을 활용하여 각 stockProduct별 최신 뉴스 조회
		return queryFactory
			.selectFrom(stockNews)
			.leftJoin(stockNews.stockProduct, stockProduct).fetchJoin()
			.where(
				Expressions.list(stockNews.stockProduct.id, stockNews.createdAt).in(
					JPAExpressions // 서브쿼리
						.select(subStockNews.stockProduct.id,
							subStockNews.createdAt.max()) // 각 상품 ID와 해당 상품의 가장 최근 일시 선택
						.from(subStockNews)
						.groupBy(subStockNews.stockProduct.id) // 상품 ID 별로 그룹화
				)
			)
			.fetch();
	}

}
