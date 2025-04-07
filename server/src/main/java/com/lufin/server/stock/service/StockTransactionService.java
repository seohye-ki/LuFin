package com.lufin.server.stock.service;

import java.util.List;

import com.lufin.server.member.domain.Member;
import com.lufin.server.stock.dto.StockTransactionRequestDto;
import com.lufin.server.stock.dto.StockTransactionResponseDto;

public interface StockTransactionService {
	// 특정 유저의 전체 주식 내역 조회
	List<StockTransactionResponseDto.TransactionDetailDto> getAllTransactionByMemberId(
		Member currentMember,
		Integer classId
	);

	// 주식 거래
	StockTransactionResponseDto.TransactionInfoDto transactStock(
		StockTransactionRequestDto.TransactionInfoDto request,
		Member currentMember,
		Integer stockProductId,
		Integer classId
	);

}
