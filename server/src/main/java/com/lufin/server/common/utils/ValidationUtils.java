package com.lufin.server.common.utils;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.lufin.server.common.exception.BusinessException;

/**
 * 검증 관련 유틸리티 클래스
 * <p>
 *  JWT 유효성 검증 기능을 제공
 */
public class ValidationUtils {

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

	private ValidationUtils() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	// 이메일 검증
	public static boolean isValidEmail(String email) {
		return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
	}

	// 비밀번호 검증
	public static boolean isValidPassword(String password) {
		return StringUtils.hasText(password) && PASSWORD_PATTERN.matcher(password).matches();
	}

	// 2차 비밀번호 검증
	public static boolean isValidSecondaryPassword(String secondaryPassword) {
		return StringUtils.hasText(secondaryPassword) && SECONDARY_PASSWORD_PATTERN.matcher(secondaryPassword)
			.matches();
	}

	// JWT: 문자열 ID가 Integer로 변환 가능한지 확인 (null, 공백, 숫자 아닌 경우 예외)
	public static void validateIntegerId(String idValue) {
		if (!StringUtils.hasText(idValue)) {
			throw new BusinessException(INVALID_INPUT_VALUE);
		}
		try {
			Integer.parseInt(idValue);
		} catch (NumberFormatException e) {
			throw new BusinessException(INVALID_INPUT_VALUE);
		}
	}
}
