package com.lufin.server.classroom.util;

import java.security.SecureRandom;

public class ClassCodeGenerator {

	private static final SecureRandom RANDOM = new SecureRandom();
	private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMBERS = "0123456789";
	private static final String ALL_CHARACTERS = UPPERCASE_LETTERS + LOWERCASE_LETTERS + NUMBERS;

	/**
	 * 영어 대소문자와 숫자가 혼합된 5자리 클래스 코드 생성
	 * 예: 3a2B5, E43ps, x7Y9q 등
	 * @return 생성된 클래스 코드
	 */
	public static String generateClassCode() {
		StringBuilder sb = new StringBuilder(5);

		// 최소 1개의 대문자, 1개의 소문자, 1개의 숫자를 포함하도록 보장
		sb.append(UPPERCASE_LETTERS.charAt(RANDOM.nextInt(UPPERCASE_LETTERS.length())));
		sb.append(LOWERCASE_LETTERS.charAt(RANDOM.nextInt(LOWERCASE_LETTERS.length())));
		sb.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));

		// 나머지 2자리는 모든 문자 집합에서 랜덤하게 선택
		for (int i = 0; i < 2; i++) {
			sb.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
		}

		// 문자열 순서를 섞어서 패턴을 예측하기 어렵게 함
		char[] chars = sb.toString().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			int randomIndex = RANDOM.nextInt(chars.length);
			char temp = chars[i];
			chars[i] = chars[randomIndex];
			chars[randomIndex] = temp;
		}

		return new String(chars);
	}
}
