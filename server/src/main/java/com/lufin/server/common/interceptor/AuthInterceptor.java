package com.lufin.server.common.interceptor;

import static com.lufin.server.common.constants.ErrorCode.*;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.common.utils.TokenUtils;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.repository.MemberRepository;
import com.lufin.server.member.support.UserContext;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	private final TokenUtils tokenUtils;
	private final MemberRepository memberRepository;

	public AuthInterceptor(TokenUtils tokenUtils, MemberRepository memberRepository) {
		this.tokenUtils = tokenUtils;
		this.memberRepository = memberRepository;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		String token = request.getHeader("Authorization");
		if (token == null || !token.startsWith("Bearer ")) {
			throw new BusinessException(INVALID_AUTH_HEADER);
		}

		Claims claims = tokenUtils.extractClaims(token);
		int userId = Integer.parseInt(claims.getSubject());

		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

		// TODO: 회원 상태 체크 추가 (탈퇴여부)

		UserContext.set(member);
		return true;
	}
}
