package com.lufin.server.websocket.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.lufin.server.websocket.model.NotificationMessage;
import com.lufin.server.websocket.model.NotificationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

	private final SimpMessagingTemplate messagingTemplate;

	/**
	 * 사용자에게 개인 알림 메시지를 전송합니다.
	 * /user/{userId}/queue/notifications 형태로 전송됩니다.
	 *
	 * @param message 전송할 알림 메시지
	 */
	public void sendNotification(NotificationMessage message) {
		log.info("💡Sending notification to user {}: {}", message.getUserId(), message);

		// 사용자별 큐로 알림 전송 (/user/{userId}/queue/notifications)
		messagingTemplate.convertAndSendToUser(
			message.getUserId(),
			"/queue/notifications",
			message
		);
	}

	/**
	 * 주제를 구독하는 모든 사용자에게 알림 메시지를 전송합니다.
	 * /topic/{topicName} 형태로 전송됩니다.
	 *
	 * @param topic 토픽 이름
	 * @param message 전송할 알림 메시지
	 */
	public void sendTopicNotification(String topic, NotificationMessage message) {
		log.info("💡Sending notification to topic {}: {}", topic, message);

		// 토픽으로 알림 전송 (/topic/{topicName})
		messagingTemplate.convertAndSend("/topic/" + topic, message);
	}

	/**
	 * 특정 큐를 구독하는 모든 사용자에게 알림 메시지를 전송합니다.
	 * /queue/{queueName} 형태로 전송됩니다.
	 *
	 * @param queue 큐 이름
	 * @param message 전송할 알림 메시지
	 */
	public void sendQueueNotification(String queue, NotificationMessage message) {
		log.info("💡Sending notification to queue {}: {}", queue, message);

		// 큐로 알림 전송 (/queue/{queueName})
		messagingTemplate.convertAndSend("/queue/" + queue, message);
	}

	/**
	 * 거래 관련 알림을 생성하고 전송합니다.
	 *
	 * @param userId 수신자 ID
	 * @param title 알림 제목
	 * @param message 알림 내용
	 * @param transactionData 거래 관련 데이터
	 */
	public void sendTransactionNotification(String userId, String title, String message, Object transactionData) {
		NotificationMessage notification = NotificationMessage.transaction(userId, title, message, transactionData);
		sendNotification(notification);
	}

	/**
	 * 신용 관련 알림을 생성하고 전송합니다.
	 *
	 * @param userId 수신자 ID
	 * @param title 알림 제목
	 * @param message 알림 내용
	 * @param creditData 신용 관련 데이터
	 */
	public void sendCreditNotification(String userId, String title, String message, Object creditData) {
		NotificationMessage notification = NotificationMessage.credit(userId, title, message, creditData);
		sendNotification(notification);
	}

	/**
	 * 미션 관련 알림을 생성하고 전송합니다.
	 *
	 * @param userId 수신자 ID
	 * @param title 알림 제목
	 * @param message 알림 내용
	 * @param missionData 미션 관련 데이터
	 */
	public void sendMissionNotification(String userId, String title, String message, Object missionData) {
		NotificationMessage notification = NotificationMessage.mission(userId, title, message, missionData);
		sendNotification(notification);
	}

	/**
	 * 급여 관련 알림을 생성하고 전송합니다.
	 *
	 * @param userId 수신자 ID
	 * @param title 알림 제목
	 * @param message 알림 내용
	 * @param wageData 급여 관련 데이터
	 */
	public void sendWageNotification(String userId, String title, String message, Object wageData) {
		NotificationMessage notification = NotificationMessage.of(
			userId,
			NotificationType.WAGE,
			title,
			message,
			wageData
		);
		sendNotification(notification);
	}

	/**
	 * 투자 관련 알림을 생성하고 전송합니다.
	 *
	 * @param userId 수신자 ID
	 * @param title 알림 제목
	 * @param message 알림 내용
	 * @param investmentData 투자 관련 데이터
	 */
	public void sendInvestmentNotification(String userId, String title, String message, Object investmentData) {
		NotificationMessage notification = NotificationMessage.of(
			userId,
			NotificationType.INVESTMENT,
			title,
			message,
			investmentData
		);
		sendNotification(notification);
	}

	/**
	 * 오류 관련 알림을 생성하고 전송합니다.
	 *
	 * @param userId 수신자 ID
	 * @param title 알림 제목
	 * @param message 알림 내용
	 * @param errorData 오류 관련 데이터
	 */
	public void sendErrorNotification(String userId, String title, String message, Object errorData) {
		NotificationMessage notification = NotificationMessage.error(
			userId,
			NotificationType.ERROR,
			title,
			message,
			errorData
		);
		sendNotification(notification);
	}

	/**
	 * 대출 관련 알림을 생성하고 전송합니다.
	 *
	 * @param userId 수신자 ID
	 * @param title 알림 제목
	 * @param message 알림 내용
	 * @param loanData 대출 관련 데이터
	 */
	public void sendLoanNotification(String userId, String title, String message, Object loanData) {
		NotificationMessage notification = NotificationMessage.loan(
			userId,
			title,
			message,
			loanData
		);
		sendNotification(notification);
	}
}
