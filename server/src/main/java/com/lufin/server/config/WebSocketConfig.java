package com.lufin.server.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 웹소켓 연결 엔드포인트 설정
		registry.addEndpoint("/ws")
			// CORS 설정
			.setAllowedOrigins("https://j12a402.p.ssafy.io:8080")
			.withSockJS();  // SockJS 지원 추가
	}

	// 메시지 브로커 설정
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		// 클라이언트가 구독할 주제 Prefix 설정
		// 클라이언트가 이 prefix로 시작하는 주제를 구독할 수 있음 (topic: 주식 관련 / queue: 학생 -> 교사)
		registry.enableSimpleBroker("/topic", "/queue");
		// 서버로 메시지를 보낼 때 사용할 prefix 설정
		registry.setApplicationDestinationPrefixes("/app");

		// 특정 사용자에게 메시지를 보내기 위한 Prefix 설정 (교사 -> 학생)
		registry.setUserDestinationPrefix("/user");
	}

	// JSON 직렬화/역직렬화를 위한 메시지 컨버터 설정
	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		// Jackson으로 JSON 변환 처리
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		// Java 8 날짜/시간 타입 지원을 위한 모듈 등록
		converter.setObjectMapper(new com.fasterxml.jackson.databind.ObjectMapper()
			.findAndRegisterModules());
		messageConverters.add(converter);
		return false;
	}
}
