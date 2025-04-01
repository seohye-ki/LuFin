package com.lufin.server.classroom.factory;

import org.springframework.stereotype.Component;

import com.lufin.server.account.domain.Account;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.classroom.dto.LoginWithClassResponse;
import com.lufin.server.classroom.dto.TokenResponse;
import com.lufin.server.common.utils.TokenUtils;
import com.lufin.server.member.domain.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResponseFactory {

	private final TokenUtils tokenUtils;

	public LoginWithClassResponse createLoginWithClassResponse(Member teacher, Classroom classroom, Account account) {
		ClassResponse classInfo = new ClassResponse(
			classroom.getId(),
			classroom.getName(),
			classroom.getSchool(),
			classroom.getCreatedAt().getYear(),
			classroom.getGrade(),
			classroom.getClassGroup(),
			classroom.getCode(),
			account.getBalance()
		);

		String accessToken = tokenUtils.createAccessToken(teacher.getId(), teacher.getMemberRole(), classroom.getId());
		String refreshToken = tokenUtils.createRefreshToken(teacher.getId(), teacher.getMemberRole(),
			classroom.getId());

		TokenResponse token = new TokenResponse(accessToken, refreshToken, teacher.getMemberRole().name(),
			classroom.getId(), account.getAccountNumber());

		return new LoginWithClassResponse(token, classInfo);
	}
}
