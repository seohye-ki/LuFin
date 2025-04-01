package com.lufin.server.websocket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.lufin.server.websocket.service.ClassSubscriptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ClassSubscriptionController {

	private final ClassSubscriptionService classSubscriptionService;

	/**
	 * 클래스 구독 시작 처리
	 * 클라이언트는 /app/class/{classId}/subscribe 엔드포인트로 메시지를 보냄
	 */
	@MessageMapping("/class/{classId}/subscribe")
	public void subscribeToClass(
		@DestinationVariable Integer classId,
		SimpMessageHeaderAccessor headerAccessor) {

		Integer userId = (Integer)headerAccessor.getSessionAttributes().get("userId");
		if (userId != null) {
			log.info("💡 User {} subscribing to class {}", userId, classId);
			classSubscriptionService.subscribeUserToClass(userId, classId);
		} else {
			log.warn("💡 Cannot subscribe to class: User ID not found in session");
		}
	}

	/**
	 * 클래스 구독 종료 처리
	 * 클라이언트는 /app/class/{classId}/unsubscribe 엔드포인트로 메시지를 보냄
	 */
	@MessageMapping("/class/{classId}/unsubscribe")
	public void unsubscribeFromClass(
		@DestinationVariable Integer classId,
		SimpMessageHeaderAccessor headerAccessor) {

		Integer userId = (Integer)headerAccessor.getSessionAttributes().get("userId");
		if (userId != null) {
			log.info("💡 User {} unsubscribing from class {}", userId, classId);
			classSubscriptionService.unsubscribeUserFromClass(userId, classId);
		} else {
			log.warn("💡 Cannot unsubscribe from class: User ID not found in session");
		}
	}
}
