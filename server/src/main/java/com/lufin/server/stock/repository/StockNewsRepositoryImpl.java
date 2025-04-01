package com.lufin.server.stock.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.stock.dto.StockNewsResponseDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockNewsRepositoryImpl implements StockNewsRepository {
	@Override
	public List<StockNewsResponseDto.NewsInfoDto> getAllNews() {
		return List.of();
	}

	@Override
	public StockNewsResponseDto.NewsInfoDto getNewsByNewsId(Integer newsId) {
		return null;
	}
}
