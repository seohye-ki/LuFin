package com.lufin.server.member.support;

import com.lufin.server.member.domain.Member;

public class UserContext {

	private static final ThreadLocal<Member> currentMember = new ThreadLocal<>();

	public static void set(Member member) {
		currentMember.set(member);
	}

	public static Member get() {
		return currentMember.get();
	}

	public static void clear() {
		currentMember.remove();
	}
}
