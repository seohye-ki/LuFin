package com.lufin.server.stock.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.classroom.domain.QClassroom;
import com.lufin.server.member.domain.QMember;
import com.lufin.server.stock.domain.QStockPortfolio;
import com.lufin.server.stock.domain.QStockProduct;
import com.lufin.server.stock.dto.QStockPortfolioResponseDto_PortfolioInfoDto;
import com.lufin.server.stock.dto.StockPortfolioResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockPortfolioRepositoryCustomImpl implements StockPortfolioRepositoryCustom {
	private final JPAQueryFactory queryFactory;

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

		/* 계산 필드 정의 */
		// 순 투자 금액 = 총 매수 금액 - 총 매도 금액
		NumberExpression<Integer> totalInvestAmount = portfolio.totalPurchaseAmount.subtract(portfolio.totalSellAmount);
		// 평균 단가 = 순 투자 금액 / 총 보유 수량
		NumberExpression<Double> averagePrice = totalInvestAmount.divide(portfolio.quantity.coalesce(1)).doubleValue();

		// 현재 이익 금액 -> (현재 단가 - 평균 단가) * 보유 개수
		NumberExpression<Double> currentReturn =
			portfolio.stockProduct.currentPrice.subtract(averagePrice)
				.multiply(portfolio.quantity)
				.doubleValue();

		// 수익률 -> (이익 금액 / 투자 금액) * 100
		// (구매 금액이 0인 경우 1로 대체하여 0으로 나누기 방지)
		NumberExpression<Double> currentReturnRate =
			currentReturn.multiply(100.0) // 현재 이익 금액
				.divide(totalInvestAmount.coalesce(1).doubleValue()); // 순 투자 금액

		List<StockPortfolioResponseDto.PortfolioInfoDto> result = queryFactory
			.select(new QStockPortfolioResponseDto_PortfolioInfoDto(
					portfolio.stockProduct.id,
					portfolio.quantity, // 현재 보유 수량
					portfolio.totalPurchaseAmount, // 총 매수 금액
					portfolio.totalSellAmount, // 총 매도 금액
					currentReturn, // 현재 이익 금액
					currentReturnRate, // 현재 이익률
					portfolio.createdAt,
					portfolio.updatedAt
				)
			)
			.from(portfolio)
			.innerJoin(portfolio.member, member).fetchJoin()
			.innerJoin(portfolio.stockProduct, product).fetchJoin()
			.innerJoin(portfolio.classroom, classroom).fetchJoin()
			.where(builder)
			.fetch();

		return result;
	}
}
