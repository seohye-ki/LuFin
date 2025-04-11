package com.lufin.server.member.util;

public class MaskingUtil {

	// 예: example@gmail.com -> e***e@gmail.com
	public static String maskEmail(String email) {
		int atIndex = email.indexOf('@');
		if (atIndex <= 1) {
			return email; // 이메일 형식이 아닌 경우 그대로 반환
		}

		String localPart = email.substring(0, atIndex);
		String domainPart = email.substring(atIndex + 1);

		String maskedLocal = localPart.charAt(0)
			+ "***"
			+ (localPart.length() > 1 ? localPart.charAt(localPart.length() - 1) : "");

		// 도메인은 가리지 않음
		return maskedLocal + "@" + domainPart;
	}

	public static String maskName(String name) {
		if (name == null || name.length() <= 2) {
			return name; // 2자 이하일 경우 그대로 반환
		}

		String first = name.substring(0, 1);
		String last = name.substring(name.length() - 1);
		String stars = "*".repeat(name.length() - 2);

		return first + stars + last;
	}
}
