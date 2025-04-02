package com.lufin.server.stock.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.classroom.domain.QClassroom;
import com.lufin.server.member.domain.QMember;
import com.lufin.server.stock.domain.QStockProduct;
import com.lufin.server.stock.domain.QStockTransactionHistory;
import com.lufin.server.stock.dto.QStockTransactionResponseDto_TransactionDetailDto;
import com.lufin.server.stock.dto.StockTransactionResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockTransactionRepositoryImpl implements StockTransactionRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<StockTransactionResponseDto.TransactionDetailDto> findAllByMemberId(Integer memberId, Integer classId) {
		QMember member = QMember.member;
		QStockProduct product = QStockProduct.stockProduct;
		QStockTransactionHistory history = QStockTransactionHistory.stockTransactionHistory;
		QClassroom classroom = QClassroom.classroom;

		BooleanBuilder builder = new BooleanBuilder();

		builder.and(member.id.eq(memberId));
		builder.and(classroom.id.eq(classId));

		List<StockTransactionResponseDto.TransactionDetailDto> result = queryFactory
			.select(new QStockTransactionResponseDto_TransactionDetailDto(
					history.id,
					history.stockProduct.id,
					member.id,
					history.type,
					history.quantity,
					history.price,
					history.totalPrice,
					history.createdAt,
					history.updatedAt
				)
			)
			.from(history)
			.leftJoin(history.member, member).fetchJoin()
			.leftJoin(history.stockProduct, product).fetchJoin()
			.leftJoin(history.classroom, classroom).fetchJoin()
			.where(builder)
			.fetch();

		return result;
	}
}
