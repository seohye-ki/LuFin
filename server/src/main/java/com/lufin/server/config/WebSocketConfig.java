package com.lufin.server.config;

import org.springframework.context.annotation.Configuration;
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
		registry.addEndpoint("/ws");
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
}
