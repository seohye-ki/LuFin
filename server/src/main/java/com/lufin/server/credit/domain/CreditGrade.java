package com.lufin.server.credit.domain;

import java.util.Arrays;

public enum CreditGrade {
	A_PLUS(95, 100),
	A(90, 94),
	A_MINUS(85, 89),
	B_PLUS(80, 84),
	B(75, 79),
	B_MINUS(70, 74),
	C_PLUS(65, 65),
	C(60, 64),
	C_MINUS(55, 59),
	D_PLUS(50, 54),
	D(45, 49),
	D_MINUS(40, 44),
	F_PLUS(35, 39),
	F(30, 34),
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
