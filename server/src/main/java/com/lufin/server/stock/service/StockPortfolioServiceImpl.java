package com.lufin.server.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.member.domain.Member;
import com.lufin.server.stock.dto.StockPortfolioResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockPortfolioServiceImpl implements StockPortfolioService {
	@Override
	public List<StockPortfolioResponseDto.PortfolioInfoDto> getPortfolio(Member currentMember, Integer classId) {
		return List.of();
	}
}
