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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		String method = request.getMethod();
		String uri = request.getRequestURI();

		log.debug("[AuthInterceptor] 요청 URI: {} {}", method, uri);

		// OPTIONS 요청은 바로 통과 (CORS preflight)
		if ("OPTIONS".equalsIgnoreCase(method)) {
			log.debug("[AuthInterceptor] OPTIONS 요청은 인증 없이 통과");
			return true;
		}

		if (isExcluded(uri)) {
			return true;
		}

		String token = request.getHeader("Authorization");
		if (token == null || !token.startsWith("Bearer ")) {
			log.warn("[AuthInterceptor] Authorization 헤더가 유효하지 않음: {}", token);
			throw new BusinessException(INVALID_AUTH_HEADER);
		}

		Claims claims = tokenUtils.extractClaims(token);
		log.debug("[AuthInterceptor] Claims 추출 완료: {}", claims);

		int userId = Integer.parseInt(claims.getSubject());
		log.debug("[AuthInterceptor] userId 파싱 완료: {}", userId);

		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> {
				log.warn("[AuthInterceptor] 존재하지 않는 사용자: {}", userId);
				return new BusinessException(MEMBER_NOT_FOUND);
			});

		if (member.getActivationStatus() != 1) {
			log.warn("[AuthInterceptor] 비활성화된 사용자 접근 시도: {}", userId);
			throw new BusinessException(MEMBER_ALREADY_DELETED);
		}

		log.debug("[AuthInterceptor] 인증 완료, 사용자 정보 설정: {}", member.getName());
		UserContext.set(member);
		return true;
	}

	private boolean isExcluded(String uri) {
		return uri.startsWith("/api/v1/lufin/auth/login")
			|| uri.startsWith("/api/v1/lufin/register")
			|| uri.startsWith("/api/v1/lufin/refresh-token")
			|| uri.startsWith("/ws")
			|| uri.startsWith("/app");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) {
		UserContext.clear();
	}
}
