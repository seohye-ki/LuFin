package com.lufin.server.websocket.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.lufin.server.websocket.model.NotificationMessage;
import com.lufin.server.websocket.model.NotificationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassSubscriptionService {

	private final NotificationService notificationService;

	// 클래스별 구독 사용자 관리 (classId -> Set<userId>)
	private final Map<Integer, Set<Integer>> classSubscriptions = new ConcurrentHashMap<>();

	/**
	 * 사용자를 클래스 구독 목록에 추가
	 */
	public void subscribeUserToClass(Integer userId, Integer classId) {
		classSubscriptions.computeIfAbsent(classId, k -> ConcurrentHashMap.newKeySet()).add(userId);
		log.info("💡 User {} subscribed to class {}", userId, classId);

		// 선택적: 클래스 구독 성공 알림 전송
		notificationService.sendNotification(NotificationMessage.of(
			userId,
			NotificationType.GENERAL,
			"클래스 구독 시작",
			classId + " 클래스 이벤트 알림을 받기 시작합니다.",
			Map.of("classId", classId)
		));
	}

	/**
	 * 사용자를 클래스 구독 목록에서 제거
	 */
	public void unsubscribeUserFromClass(Integer userId, Integer classId) {
		Set<Integer> subscribers = classSubscriptions.get(classId);
		if (subscribers != null) {
			subscribers.remove(userId);
			log.info("💡 User {} unsubscribed from class {}", userId, classId);
		}
	}

	/**
	 * 특정 이벤트가 발생한 학생에게 알림 전송
	 * 해당 학생이 클래스를 구독 중인지 확인 후 전송
	 */
	public void sendEventNotificationToStudent(Integer classId, Integer studentId,
		NotificationType eventType,
		String title, String message,
		Object eventData) {
		Set<Integer> subscribers = classSubscriptions.get(classId);
		if (subscribers != null && subscribers.contains(studentId)) {
			switch (eventType) {
				case LOAN:
					notificationService.sendLoanNotification(studentId, title, message, eventData);
					break;
				case INVESTMENT:
					notificationService.sendInvestmentNotification(studentId, title, message, eventData);
					break;
				case CREDIT:
					notificationService.sendCreditNotification(studentId, title, message, eventData);
					break;
				case MISSION:
					notificationService.sendMissionNotification(studentId, title, message, eventData);
					break;
				case WAGE:
					notificationService.sendWageNotification(studentId, title, message, eventData);
					break;
				case TRANSACTION:
					notificationService.sendTransactionNotification(studentId, title, message, eventData);
					break;
				case ERROR:
					notificationService.sendErrorNotification(studentId, title, message, eventData);
					break;
				default:
					notificationService.sendNotification(NotificationMessage.of(
						studentId, eventType, title, message, eventData));
			}
			log.info("💡 Sent {} notification to student {} in class {}", eventType, studentId, classId);
		} else {
			log.warn("💡 Student {} not subscribed to class {}", studentId, classId);
		}
	}

	/**
	 * 클래스에 구독 중인 학생인지 확인
	 */
	public boolean isStudentSubscribedToClass(Integer classId, Integer studentId) {
		Set<Integer> subscribers = classSubscriptions.get(classId);
		return subscribers != null && subscribers.contains(studentId);
	}
}
