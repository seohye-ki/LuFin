package com.lufin.server.stock.repository;

import java.util.List;

import com.lufin.server.stock.dto.StockNewsResponseDto;

public interface StockNewsRepositoryCustom {
	// 공시 정보 목록 조회
	List<StockNewsResponseDto.NewsInfoDto> getAllNews(Integer stockProductId);

	// 공시 정보 상세 조회
	StockNewsResponseDto.NewsInfoDto getNewsByNewsId(Integer stockProductId, Integer newsId);

}
