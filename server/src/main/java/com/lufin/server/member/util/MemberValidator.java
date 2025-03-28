package com.lufin.server.member.util;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.lufin.server.common.exception.BusinessException;

public class MemberValidator {
	/**
	 * [이메일 패턴 확인]
	 * - 영문자, 숫자, 점(.), 언더스코어(_), 하이픈(-) 허용
	 * - @ 기호 필수
	 * - 도메인 부분에 영문자, 숫자, 점, 하이픈 허용
	 * - 최상위 도메인은 2-6자 사이의 영문자만 허용
	 * <p>
	 * [비밀번호 패턴 확인]
	 * - 대소문자, 숫자, 특수문자를 각각 1개 이상 포함
	 * - 전체 길이는 8 ~ 16자 사이
	 * <p>
	 * [2차 비밀번호 확인]
	 * - 숫자 6자리
	 */
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(
		"^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?/]).{8,16}$");
	private static final Pattern SECONDARY_PASSWORD_PATTERN = Pattern.compile("^\\d{6}$");

	private MemberValidator() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	// 이메일 검증
	public static void isValidEmail(String email) {
		if (!StringUtils.hasText(email)) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		} else if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new BusinessException(INVALID_EMAIL_FORMAT);
		}
	}

	// 비밀번호 검증
	public static void isValidPassword(String password) {
		if (!StringUtils.hasText(password)) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		} else if (!PASSWORD_PATTERN.matcher(password).matches()) {
			throw new BusinessException(INVALID_INPUT_VALUE);
		}
	}

	// 2차 비밀번호 검증
	public static void isValidSecondaryPassword(String secondaryPassword) {
		if (!StringUtils.hasText(secondaryPassword)) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		} else if (!SECONDARY_PASSWORD_PATTERN.matcher(secondaryPassword).matches()) {
			throw new BusinessException(INVALID_INPUT_VALUE);
		}
	}
}
