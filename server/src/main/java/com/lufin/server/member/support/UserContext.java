package com.lufin.server.member.support;

import com.lufin.server.member.domain.Member;

public class UserContext {

	private static final ThreadLocal<Member> CURRENT_MEMBER = new ThreadLocal<>();

	public static void set(Member member) {
		CURRENT_MEMBER.set(member);
	}

	public static Member get() {
		return CURRENT_MEMBER.get();
	}

	public static void clear() {
		CURRENT_MEMBER.remove();
	}
}
