package com.lufin.server.websocket.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;

@Getter
public class NotificationMessage implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private final String id;                 // 알림 고유 ID
	private final String userId;             // 수신자 ID
	private final NotificationType type;     // 알림 유형
	private final String title;              // 알림 제목
	private final String message;            // 알림 내용

	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
	private final Object payload;            // 추가 데이터 (타입별로 다름)

	private final NotificationLevel level;   // 알림 중요도
	private final LocalDateTime timestamp;   // 생성 시간

	// private 생성자
	@JsonCreator
	private NotificationMessage(
		@JsonProperty("id") String id,
		@JsonProperty("userId") String userId,
		@JsonProperty("type") NotificationType type,
		@JsonProperty("title") String title,
		@JsonProperty("message") String message,
		@JsonProperty("payload") Object payload,
		@JsonProperty("level") NotificationLevel level,
		@JsonProperty("timestamp") LocalDateTime timestamp) {
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.title = title;
		this.message = message;
		this.payload = payload;
		this.level = level;
		this.timestamp = timestamp;
	}

	// 기본 정적 팩토리 메서드
	public static NotificationMessage of(String userId, NotificationType type,
		String title, String message) {
		return new NotificationMessage(
			UUID.randomUUID().toString(),
			userId,
			type,
			title,
			message,
			null,
			NotificationLevel.INFO,
			LocalDateTime.now()
		);
	}

	// 추가 데이터가 있는 정적 팩토리 메서드
	public static NotificationMessage of(String userId, NotificationType type,
		String title, String message, Object payload) {
		return new NotificationMessage(
			UUID.randomUUID().toString(),
			userId,
			type,
			title,
			message,
			payload,
			NotificationLevel.INFO,
			LocalDateTime.now()
		);
	}

	// 모든 값을 지정할 수 있는 정적 팩토리 메서드
	public static NotificationMessage of(String userId, NotificationType type,
		String title, String message, Object payload,
		NotificationLevel level) {
		return new NotificationMessage(
			UUID.randomUUID().toString(),
			userId,
			type,
			title,
			message,
			payload,
			level,
			LocalDateTime.now()
		);
	}

	// 알림 유형별 편의 메서드
	public static NotificationMessage transaction(String userId, String title,
		String message, Object transactionData) {
		return new NotificationMessage(
			UUID.randomUUID().toString(),
			userId,
			NotificationType.TRANSACTION,
			title,
			message,
			transactionData,
			NotificationLevel.INFO,
			LocalDateTime.now()
		);
	}

	public static NotificationMessage credit(String userId, String title,
		String message, Object creditData) {
		return new NotificationMessage(
			UUID.randomUUID().toString(),
			userId,
			NotificationType.CREDIT,
			title,
			message,
			creditData,
			NotificationLevel.INFO,
			LocalDateTime.now()
		);
	}

	public static NotificationMessage mission(String userId, String title,
		String message, Object missionData) {
		return new NotificationMessage(
			UUID.randomUUID().toString(),
			userId,
			NotificationType.MISSION,
			title,
			message,
			missionData,
			NotificationLevel.INFO,
			LocalDateTime.now()
		);
	}

	// 레벨별 편의 메서드
	public static NotificationMessage success(String userId, NotificationType type,
		String title, String message, Object payload) {
		return new NotificationMessage(
			UUID.randomUUID().toString(),
			userId,
			type,
			title,
			message,
			payload,
			NotificationLevel.SUCCESS,
			LocalDateTime.now()
		);
	}

	public static NotificationMessage warning(String userId, NotificationType type,
		String title, String message, Object payload) {
		return new NotificationMessage(
			UUID.randomUUID().toString(),
			userId,
			type,
			title,
			message,
			payload,
			NotificationLevel.WARNING,
			LocalDateTime.now()
		);
	}

	public static NotificationMessage error(String userId, NotificationType type,
		String title, String message, Object payload) {
		return new NotificationMessage(
			UUID.randomUUID().toString(),
			userId,
			type,
			title,
			message,
			payload,
			NotificationLevel.ERROR,
			LocalDateTime.now()
		);
	}

	public enum NotificationLevel {
		INFO, SUCCESS, WARNING, ERROR
	}
}
