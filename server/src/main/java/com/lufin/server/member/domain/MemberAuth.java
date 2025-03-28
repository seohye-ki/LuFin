package com.lufin.server.member.domain;

import static com.lufin.server.member.util.MemberValidator.*;

import java.time.LocalDateTime;

import com.lufin.server.common.utils.HashUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Embeddable
@RequiredArgsConstructor
public class MemberAuth {

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "salt", nullable = false)
	private String salt;

	@Getter
	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@Column(name = "secondary_password", nullable = false)
	private String secondaryPassword;

	public MemberAuth(String password, String secondaryPassword) {
		isValidPassword(password);
		isValidSecondaryPassword(secondaryPassword);
		this.salt = HashUtils.generateSalt();
		this.password = HashUtils.hashPassword(password, salt);
		this.secondaryPassword = HashUtils.hashPassword(secondaryPassword, salt);
	}

	public void updateLastLogin() {
		this.lastLogin = LocalDateTime.now();
	}

	public boolean isPasswordMatch(String inputPassword) {
		String hashedPassword = HashUtils.hashPassword(inputPassword, this.salt);
		return this.password.matches(hashedPassword);
	}

	public boolean isSecondaryPasswordMatch(String inputPassword) {
		String hashedPassword = HashUtils.hashPassword(inputPassword, this.salt);
		return this.secondaryPassword.matches(hashedPassword);
	}
}
