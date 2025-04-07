package com.lufin.server.stock.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.stock.dto.StockPriceHistoryRequestDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonToDtoUtil {
	public static StockPriceHistoryRequestDto.PriceHistoryAiInfoDto convert(String content) {
		try {
			// ObjectMapper 인스턴스 생성
			ObjectMapper objectMapper = new ObjectMapper();

			log.info("Java 객체 변환 시도: content = {}", content);

			// JSON 문자열을 Java 객체로 변환
			StockPriceHistoryRequestDto.PriceHistoryAiResponseDto response = objectMapper.readValue(content,
				StockPriceHistoryRequestDto.PriceHistoryAiResponseDto.class);

			log.info("Java 객체 변환 성공: response = {}", response);

			// 변환된 객체 사용하기
			return response.priceHistoryAiInfoDto();

		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage());
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}
	}
}
