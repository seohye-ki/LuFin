package com.lufin.server.account.util;

import java.security.SecureRandom;

public class AccountNumberGenerator {

	private static final SecureRandom RANDOM = new SecureRandom();

	public static String generateAccountNumber() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			int part = RANDOM.nextInt(900) + 100; // 100~999
			sb.append(part);
			if (i < 3) {
				sb.append("-");
			}
		}
		return sb.toString();
	}
}
