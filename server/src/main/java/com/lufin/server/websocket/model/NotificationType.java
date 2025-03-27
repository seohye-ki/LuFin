package com.lufin.server.websocket.model;

public enum NotificationType {
	GENERAL,       // 일반 알림
	TRANSACTION,   // 금융 거래 관련
	CREDIT,        // 신용 점수/등급 관련
	MISSION,       // 미션 관련
	SALARY,        // 급여 관련
	INVESTMENT,    // 투자 관련
	ERROR          // 오류 알림
}
