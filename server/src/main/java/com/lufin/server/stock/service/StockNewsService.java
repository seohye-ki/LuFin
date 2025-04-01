package com.lufin.server.stock.service;

import java.util.List;

import com.lufin.server.stock.dto.StockNewsRequestDto;
import com.lufin.server.stock.dto.StockNewsResponseDto;

public interface StockNewsService {
	// 특정 주식 공시 정보 목록 조회
	List<StockNewsResponseDto.NewsInfoDto> getAllNews(Integer stockProductId);

	// 특정 주식 공시 정보 상세 조회
	StockNewsResponseDto.NewsInfoDto getNewsByNewsId(Integer stockProductId, Integer newsId);

	// 특정 주식 공시 정보 생성
	StockNewsResponseDto.NewsCreateUpdateDto createNews(Integer stockProductId,
		StockNewsRequestDto.NewsInfoDto newsInfoDto);
}
