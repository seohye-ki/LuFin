package com.lufin.server.stock.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.stock.domain.QStockNews;
import com.lufin.server.stock.dto.QStockNewsResponseDto_NewsInfoDto;
import com.lufin.server.stock.dto.StockNewsResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockNewsRepositoryImpl implements StockNewsRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<StockNewsResponseDto.NewsInfoDto> getAllNews() {
		QStockNews stockNews = QStockNews.stockNews;

		List<StockNewsResponseDto.NewsInfoDto> result = queryFactory
			.select(new QStockNewsResponseDto_NewsInfoDto(
				stockNews.id,
				stockNews.content,
				stockNews.createdAt,
				stockNews.updatedAt
			))
			.from(stockNews)
			.fetch();

		return result;
	}

	@Override
	public StockNewsResponseDto.NewsInfoDto getNewsByNewsId(Integer newsId) {
		QStockNews stockNews = QStockNews.stockNews;

		StockNewsResponseDto.NewsInfoDto result = queryFactory
			.select(new QStockNewsResponseDto_NewsInfoDto(
				stockNews.id,
				stockNews.content,
				stockNews.createdAt,
				stockNews.updatedAt
			))
			.from(stockNews)
			.where(stockNews.id.eq(newsId))
			.fetchOne();

		return result;
	}
}
