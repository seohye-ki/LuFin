package com.lufin.server.stock.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.lufin.server.classroom.domain.QClassroom;
import com.lufin.server.member.domain.QMember;
import com.lufin.server.stock.domain.QStockPortfolio;
import com.lufin.server.stock.domain.QStockProduct;
import com.lufin.server.stock.domain.StockPortfolio;
import com.lufin.server.stock.dto.StockPortfolioResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StockPortfolioRepositoryCustomImpl implements StockPortfolioRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	/**
	 * memberId와 classId에 해당하는 모든 포트폴리오 조회
	 * @param memberId
	 * @param classId
	 * @return
	 */
	@Override
	public List<StockPortfolioResponseDto.PortfolioInfoDto> findAllByMemberIdAndClassId(
		Integer memberId,
		Integer classId
	) {
		QStockPortfolio portfolio = QStockPortfolio.stockPortfolio;
		QMember member = QMember.member;
		QStockProduct product = QStockProduct.stockProduct;
		QClassroom classroom = QClassroom.classroom;

		BooleanBuilder builder = new BooleanBuilder();

		builder.and(member.id.eq(memberId));
		builder.and(classroom.id.eq(classId));
		builder.and(portfolio.quantity.gt(0)); // 보유하고 있는 주식만 조회하도록 조건 추가

		// N+1 문제를 방지하기 위해 fetch join을 사용하여 엔티티를 먼저 조회
		List<StockPortfolio> portfolios = queryFactory
			.selectFrom(portfolio)
			.innerJoin(portfolio.member, member).fetchJoin()
			.innerJoin(portfolio.stockProduct, product).fetchJoin()
			.innerJoin(portfolio.classroom, classroom).fetchJoin()
			.where(builder)
			.fetch();

		if (portfolios == null || portfolios.isEmpty()) {
			return List.of();
		}

		// 엔티티를 DTO로 변환하면서 필요한 계산 수행
		return portfolios.stream()
			.map(p -> {
				// 순 투자 금액 = 총 매수 금액 - 총 매도 금액
				int totalInvestAmount = p.getTotalPurchaseAmount() - p.getTotalSellAmount();

				// 평균 단가 = 순 투자 금액 / 총 보유 수량
				double averagePrice = (double)totalInvestAmount / (p.getQuantity() != null ? p.getQuantity() : 1);

				// 현재 이익 금액 -> (현재 단가 - 평균 단가) * 보유 개수
				double currentReturn = (p.getStockProduct().getCurrentPrice() - averagePrice) * p.getQuantity();

				// 수익률 -> (이익 금액 / 투자 금액) * 100
				double currentReturnRate = currentReturn * 100.0 / (totalInvestAmount != 0 ? totalInvestAmount : 1);

				return new StockPortfolioResponseDto.PortfolioInfoDto(
					p.getStockProduct().getId(),
					p.getQuantity(),
					p.getTotalPurchaseAmount(),
					p.getTotalSellAmount(),
					currentReturn,
					currentReturnRate,
					p.getCreatedAt(),
					p.getUpdatedAt()
				);
			})
			.collect(Collectors.toList());
	}
}