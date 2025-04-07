package com.lufin.server.stock.service;

import java.util.List;

import com.lufin.server.stock.dto.ClaudeRequestDto;

public interface StockAiService {
	String generateResponse(String prompt);

	String generateResponseWithHistory(List<ClaudeRequestDto.Message> messages);
}
