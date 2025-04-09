package com.lufin.server.stock.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.QStockNews;
import com.lufin.server.stock.domain.QStockProduct;
import com.lufin.server.stock.domain.StockNews;
import com.lufin.server.stock.dto.StockNewsResponseDto;
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

}
