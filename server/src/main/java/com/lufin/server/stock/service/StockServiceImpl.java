package com.lufin.server.stock.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.stock.domain.StockProduct;
import com.lufin.server.stock.domain.StockTransactionHistory;
import com.lufin.server.stock.dto.StockPortfolioResponseDto;
import com.lufin.server.stock.repository.StockPortfolioRepository;
import com.lufin.server.stock.repository.StockProductRepository;
import com.lufin.server.stock.repository.StockRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

	private final StockRepository stockRepository;
	private final StockPortfolioRepository stockPortfolioRepository;
	private final StockProductRepository stockProductRepository;

	@Override
	public int getTotalValuation(int memberId, int classId) {
		log.info("[보유한 주식의 총 평가 금액 조회] memberId = {}", memberId);
		// 보유 중인 주식 리스트 조회 후 평가 금액 합산
		return stockRepository.findAllByMemberIdAndClassroomId(memberId, classId).stream()
			.mapToInt(StockTransactionHistory::getTotalPrice)
			.sum();
	}

	@Override
	public int getTotalValuationOnDate(int memberId, int classId, LocalDate date) {
		log.info("[{} 기준 보유 주식 평가액 조회] memberId={}, classId={}", date, memberId, classId);

		try {

			// 해당 시점까지의 모든 매수/매도 내역 조회 후 평가
			List<StockTransactionHistory> stocks = stockRepository
				.findAllByMemberIdAndClassroomIdAndCreatedAtLessThanEqual(
					memberId,
					classId,
					date.atTime(23, 59, 59)
				);

			// 주식 평가 금액 계산
			// type이 1인 totalPrice 합계에서 type이 0인 totalPrice 합계를 뺌
			int buyTotal = stocks.stream()
				.filter(stock -> stock.getType() == 1)
				.mapToInt(StockTransactionHistory::getTotalPrice)
				.sum();

			int sellTotal = stocks.stream()
				.filter(stock -> stock.getType() == 0)
				.mapToInt(StockTransactionHistory::getTotalPrice)
				.sum();

			return buyTotal - sellTotal;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage());
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}
	}

	// 각 주식의 현재 보유 개수와 그 주식의 현재 가치 조회
	@Override
	public int getTotalValuationByStocks(int memberId, int classId) {
		log.info("[보유한 주식의 총 평가 금액 조회] memberId = {}", memberId);

		try {
			// 포트폴리오 조회(주식에 대한 총 보유량 조회)
			List<StockPortfolioResponseDto.PortfolioInfoDto> stockPortfolios = stockPortfolioRepository.findAllByMemberIdAndClassId(
				memberId, classId);

			// 각 주식의 현재 가격 조회 후 Map에 저장
			Map<Integer, StockProduct> stockProductMap = stockProductRepository.findAll().stream()
				.collect(Collectors.toMap(StockProduct::getId, stockProduct -> stockProduct));

			// 주식 보유량 * 현재 주가를 각각 계산하고 전체 합산
			return stockPortfolios.stream()
				.filter(portfolio -> stockProductMap.containsKey(portfolio.stockProductId()))
				.mapToInt(portfolio ->
					portfolio.quantity() * stockProductMap.get(portfolio.stockProductId()).getCurrentPrice())
				.sum();
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage());
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}

	}

}
