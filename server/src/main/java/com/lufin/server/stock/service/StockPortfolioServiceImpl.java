package com.lufin.server.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.stock.dto.StockPortfolioResponseDto;
import com.lufin.server.stock.repository.StockPortfolioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockPortfolioServiceImpl implements StockPortfolioService {
	private final StockPortfolioRepository stockPortfolioRepository;

	/**
	 * 주식 포트폴리오 조회
	 * @param currentMember
	 * @param classId
	 */
	@Override
	public List<StockPortfolioResponseDto.PortfolioInfoDto> getPortfolio(Member currentMember, Integer classId) {
		log.info("주식 포트폴리오 조회 요청: memberId = {}, classId = {}",
			currentMember != null ? currentMember.getId() : null, classId);

		if (currentMember == null || classId == null) {
			log.warn("주식 포트폴리오 조회 요청에 필요한 값이 부족합니다.: currentMember = {}, classId = {}",
				currentMember != null ? currentMember.getId() : null, classId);
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}

		try {
			List<StockPortfolioResponseDto.PortfolioInfoDto> portfolioInfos = stockPortfolioRepository.findAllByMemberIdAndClassId(
				currentMember.getId(),
				classId
			);

			log.info("주식 포트폴리오 조회 성공: portfolioInfoSize = {}", portfolioInfos.size());

			return portfolioInfos;

		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}
	}
}
