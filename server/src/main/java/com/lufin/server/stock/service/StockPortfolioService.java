package com.lufin.server.stock.service;

import java.util.List;

import com.lufin.server.member.domain.Member;
import com.lufin.server.stock.dto.StockPortfolioResponseDto;

public interface StockPortfolioService {
	// 포트폴리오 조회
	List<StockPortfolioResponseDto.PortfolioInfoDto> getPortfolio(
		Member currentMember,
		Integer classId
	);
}
