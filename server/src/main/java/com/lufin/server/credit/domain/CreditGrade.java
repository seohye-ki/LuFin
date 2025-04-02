package com.lufin.server.credit.domain;

import java.util.Arrays;

public enum CreditGrade {
	A_PLUS(95, 100),
	A(90, 94),
	A_MINUS(89, 85),
	B_PLUS(84, 80),
	B(79, 75),
	B_MINUS(74, 70),
	C_PLUS(69, 65),
	C(64, 60),
	C_MINUS(59, 55),
	D_PLUS(54, 50),
	D(49, 45),
	D_MINUS(44, 40),
	F_PLUS(39, 35),
	F(34, 30),
	F_MINUS(0, 29);

	private final int min;
	private final int max;

	CreditGrade(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public static CreditGrade fromScore(int score) {
		return Arrays.stream(values())
			.filter(g -> score >= g.min && score <= g.max)
			.findFirst()
			.orElse(F_MINUS);
	}
}
