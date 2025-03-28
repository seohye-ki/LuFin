package com.lufin.server.common.utils;

import static com.lufin.server.common.constants.ErrorCode.*;

import org.springframework.util.StringUtils;

import com.lufin.server.common.exception.BusinessException;

/**
 * 검증 관련 유틸리티 클래스
 * <p>
 *  JWT 유효성 검증 기능을 제공
 */
public class ValidationUtils {

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
