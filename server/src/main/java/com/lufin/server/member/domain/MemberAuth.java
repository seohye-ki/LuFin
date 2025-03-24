package com.lufin.server.member.domain;

import java.time.LocalDateTime;

import com.lufin.server.common.utils.HashUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.RequiredArgsConstructor;

@Embeddable
@RequiredArgsConstructor
public class MemberAuth {

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "salt", nullable = false, length = 255)
	private String salt;

	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@Column(name = "secondary_password", nullable = false, length = 255)
	private String secondaryPassword;

	public MemberAuth(String password, String secondaryPassword) {
		this.salt = HashUtils.generateSalt();
		this.password = HashUtils.hashPassword(password, salt);
		this.secondaryPassword = HashUtils.hashPassword(secondaryPassword, salt);
	}

	public void updateLastLogin() {
		this.lastLogin = LocalDateTime.now();
	}
}
