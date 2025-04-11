package com.lufin.server.common.utils;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.util.StringUtils;

import com.lufin.server.common.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;

/**
 * 해시 관련 유틸리티 클래스
 * <p>
 * PBKDF2 + HMAC-SHA256 해시 변환, 솔트 생성 기능을 제공
 */
@Slf4j
public class HashUtils {

	private static final int ITERATION_COUNT = 10000; // 반복 횟수 (KISA 권장: 10,000 이상)
	private static final int KEY_LENGTH = 256; // 해시 길이 (256비트 = 32바이트)

	public static String hashPassword(String password, String salt) {
		if (!StringUtils.hasText(salt) || !StringUtils.hasText(password)) {
			log.warn("⚠️ [HashUtils] 비밀번호 또는 salt 누락 - password: {}, salt: {}",
				password == null ? "null" : "provided",
				salt == null ? "null" : "provided");
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}
		try {
			log.debug("[HashUtils] 해시 생성 시작 - 반복횟수: {}, 해시길이: {}", ITERATION_COUNT, KEY_LENGTH);

			// 1. PBEKeySpec 객체 생성 (비밀번호, salt, 반복 횟수, 해시 길이 설정)
			PBEKeySpec spec = new PBEKeySpec(
				password.toCharArray(),                 // 비밀번호를 char 배열로 변환
				Base64.getDecoder().decode(salt),       // Base64로 인코딩된 salt를 디코딩하여 사용
				ITERATION_COUNT,                        // 반복 횟수 (높을수록 연산량 증가)
				KEY_LENGTH                              // 해시 길이 (256비트)
			);

			// 2. PBKDF2WithHmacSHA256 알고리즘을 사용하여 해시 생성
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			byte[] hash = skf.generateSecret(spec).getEncoded(); // 해싱 수행 후 바이트 배열로 반환
			log.debug("[HashUtils] 해시 생성 완료");

			// 3. 해시 값을 Base64 문자열로 변환하여 반환
			return Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			// 4. 예외 발생 시 실행 (예: 알고리즘 지원되지 않음, 입력 오류 등)
			log.error("⚠️[HashUtils] 비밀번호 해싱 실패", e);
			throw new BusinessException(SERVER_ERROR);
		}
	}

	//솔트 생성 메서드
	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}
}
