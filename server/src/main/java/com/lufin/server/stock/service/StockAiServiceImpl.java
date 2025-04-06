package com.lufin.server.stock.service;

import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.lufin.server.config.ClaudeApiConfig;
import com.lufin.server.stock.dto.ClaudeRequestDto;
import com.lufin.server.stock.dto.ClaudeResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockAiServiceImpl implements StockAiService {

	private final WebClient claudeWebClient;
	private final ClaudeApiConfig claudeApiConfig;

	/**
	 * 단일 프롬프트에 대한 응답을 생성합니다.
	 * @param prompt 사용자 프롬프트
	 * @return Claude API의 응답 텍스트
	 */
	@Override
	public String generateResponse(String prompt) {
		// 사용자 메시지 생성
		ClaudeRequestDto.Message userMessage = ClaudeRequestDto.Message.builder()
			.role("user")
			.content(prompt)
			.build();

		// 기존 대화 이력 메소드 활용 (코드 재사용)
		return generateResponseWithHistory(Collections.singletonList(userMessage));
	}

	/**
	 * 대화 이력을 포함한 복잡한 요청에 대한 응답을 생성합니다.
	 * @param messages 대화 메시지 목록
	 * @return Claude API의 응답 텍스트
	 */
	@Override
	public String generateResponseWithHistory(List<ClaudeRequestDto.Message> messages) {
		// API 요청 DTO 생성
		ClaudeRequestDto requestDto = ClaudeRequestDto.builder()
			.model(claudeApiConfig.getModel())         // 설정된 모델 사용
			.messages(messages)                         // 대화 메시지 목록
			.max_tokens(claudeApiConfig.getMaxTokens()) // 최대 토큰 수
			.temperature(claudeApiConfig.getTemperature()) // 온도 설정
			.build();

		log.debug("Claude API 요청: {}", requestDto);  // 디버그 로깅

		try {
			// WebClient를 사용하여 API 호출
			ClaudeResponseDto responseDto = claudeWebClient.post()
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestDto)
				.retrieve()
				.bodyToMono(ClaudeResponseDto.class)  // 응답을 DTO로 매핑
				.block();  // 비동기 호출을 동기적으로 처리

			// 응답 확인 및 반환
			if (responseDto != null && responseDto.getContent() != null) {
				log.debug("Claude API 응답: {}", responseDto.getId());
				return responseDto.getContent().getText();  // 응답 텍스트 반환
			} else {
				log.error("Claude API 응답이 올바르지 않습니다");
				throw new RuntimeException("Claude API 응답이 올바르지 않습니다");
			}
		} catch (Exception e) {
			// 오류 로깅 및 예외 처리
			log.error("Claude API 호출 중 오류 발생", e);
			throw new RuntimeException("Claude API 호출 중 오류가 발생했습니다: " + e.getMessage(), e);
		}

	}

}
