package com.lufin.server.stock.repository;

import java.util.List;

import com.lufin.server.stock.dto.StockTransactionResponseDto;

public interface StockTransactionRepositoryCustom {
	List<StockTransactionResponseDto.TransactionDetailDto> findAllByMemberId(Integer memberId, Integer classId);
}
