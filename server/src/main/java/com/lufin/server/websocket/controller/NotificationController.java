package com.lufin.server.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.lufin.server.websocket.model.NotificationMessage;
import com.lufin.server.websocket.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

	private final NotificationService notificationService;

	/**
	 * 클라이언트로부터 알림 요청을 받아 처리합니다.
	 * 클라이언트는 /app/notification 엔드포인트로 메시지를 보낼 수 있습니다.
	 * 이 메시지는 개별 사용자에게 전송됩니다.
	 *
	 * @param message 클라이언트가 전송한 알림 메시지
	 * @param headerAccessor 메시지 헤더 정보
	 */
	@MessageMapping("/notification")
	public void processNotification(@Payload NotificationMessage message, SimpMessageHeaderAccessor headerAccessor) {
		log.info("💡Received notification request: {}", message);
		notificationService.sendNotification(message);
	}

	/**
	 * 주제를 구독하는 모든 사용자에게 메시지를 전송하기 위한 엔드포인트
	 * 클라이언트는 /app/notification/topic 엔드포인트로 메시지를 보낼 수 있습니다.
	 * 메시지는 /topic/general로 전송됩니다.
	 */
	@MessageMapping("/notification/topic")
	@SendTo("/topic/general")
	public NotificationMessage sendToTopic(@Payload NotificationMessage message) {
		log.info("💡Received notification to broadcast to topic: {}", message);
		return message;
	}

	/**
	 * 특정 주제를 구독하는 모든 사용자에게 메시지를 전송하기 위한 엔드포인트
	 * 클라이언트는 /app/notification/topic/{topicName} 엔드포인트로 메시지를 보낼 수 있습니다.
	 */
	@MessageMapping("/notification/topic/{topicName}")
	public void sendToSpecificTopic(
		@Payload NotificationMessage message,
		SimpMessageHeaderAccessor headerAccessor) {

		log.info("💡Received notification to broadcast to specific topic: {}", message);

		String destination = headerAccessor.getDestination();
		if (destination == null || !destination.startsWith("/app/notification/topic/")) {
			log.warn("💡Invalid destination for topic notification: {}", destination);
			return;
		}

		String topicName = destination.substring("/app/notification/topic/".length());
		notificationService.sendTopicNotification(topicName, message);
	}

	/**
	 * 특정 큐를 구독하는 모든 사용자에게 메시지를 전송하기 위한 엔드포인트
	 * 클라이언트는 /app/notification/queue/{queueName} 엔드포인트로 메시지를 보낼 수 있습니다.
	 */
	@MessageMapping("/notification/queue/{queueName}")
	public void sendToQueue(
		@Payload NotificationMessage message,
		SimpMessageHeaderAccessor headerAccessor) {

		log.info("💡Received notification to send to queue: {}", message);

		String destination = headerAccessor.getDestination();
		if (destination == null || !destination.startsWith("/app/notification/queue/")) {
			log.warn("💡Invalid destination for queue notification: {}", destination);
			return;
		}

		String queueName = destination.substring("/app/notification/queue/".length());
		notificationService.sendQueueNotification(queueName, message);
	}
}
