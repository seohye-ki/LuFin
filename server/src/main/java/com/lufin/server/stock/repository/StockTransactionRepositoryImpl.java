package com.lufin.server.stock.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufin.server.classroom.domain.QClassroom;
import com.lufin.server.member.domain.QMember;
import com.lufin.server.stock.domain.QStockProduct;
import com.lufin.server.stock.domain.QStockTransactionHistory;
import com.lufin.server.stock.domain.StockTransactionHistory;
import com.lufin.server.stock.dto.StockTransactionResponseDto;
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

		List<StockTransactionHistory> transactionHistories = queryFactory
			.selectFrom(history).distinct()
			.leftJoin(history.stockProduct, product)
			.leftJoin(history.classroom, classroom)
			.where(history.member.id.eq(memberId))
			.fetch();

		return transactionHistories.stream()
			.map(StockTransactionResponseDto.TransactionDetailDto::stockTransactionHistoryEntityToTransactionDetailDto)
			.toList();
	}
}
