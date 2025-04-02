package com.lufin.server.stock.service;

import com.lufin.server.member.domain.Member;
import com.lufin.server.stock.dto.StockTransactionRequestDto;
import com.lufin.server.stock.dto.StockTransactionResponseDto;

public interface StockTransactionService {
	// 주식 구매
	StockTransactionResponseDto.TransactionInfoDto transactStock(
		StockTransactionRequestDto.TransactionInfoDto request,
		Member currentMember,
		Integer stockProductId,
		Integer classId
	);

	// 특정 유저의 전체 주식 내역 조회
	StockTransactionResponseDto.TransactionDetailDto getAllTransactionByMemberId(
		Member currentMember
	);
}
