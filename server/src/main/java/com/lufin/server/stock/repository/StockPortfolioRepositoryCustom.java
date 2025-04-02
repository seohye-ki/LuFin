package com.lufin.server.stock.repository;

import java.util.List;

import com.lufin.server.stock.dto.StockPortfolioResponseDto;

public interface StockPortfolioRepositoryCustom {
	List<StockPortfolioResponseDto.PortfolioInfoDto> findAllByMemberIdAndClassId(Integer memberId, Integer classId);
}
