package com.lufin.server.common.filter;

import static com.lufin.server.common.constants.ErrorCode.*;
import static com.lufin.server.common.utils.ValidationUtils.*;

import java.io.IOException;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.lufin.server.common.constants.TokenClaimName;
import com.lufin.server.common.constants.TokenType;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.common.utils.TokenUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT를 직접 파싱하여 userId, tokenType, classId 유효성을 검증하는 필터
 * classId는 서비스 계층에서 필요한 경우 직접 검증.
 */
@Component
public class JwtFilter implements Filter {

	private final TokenUtils tokenUtils;

	// 필터를 적용하지 않을 경로들 (로그인, 회원가입 등)
	private final Set<String> excludedPaths = Set.of("/auth/login", "/register");

	public JwtFilter(TokenUtils tokenUtils) {
		this.tokenUtils = tokenUtils;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String requestURI = httpRequest.getRequestURI();

		// 필터 예외 경로는 바로 통과
		if (excludedPaths.contains(requestURI)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			// JWT 기반 헤더 검증
			validateToken(httpRequest);
			filterChain.doFilter(request, response);

		} catch (BusinessException e) {
			// 검증 실패 시 401 반환
			httpResponse.sendError(401, "인증이 필요합니다.");
		}
	}

	// userId, tokenType, classId 유효성 검증
	private void validateToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new BusinessException(INVALID_AUTH_HEADER);
		}

		// JWT에서 Claims 추출
		Claims claims = tokenUtils.extractClaims(authorizationHeader);

		String userId = claims.getSubject();
		validateIntegerId(userId);

		// tokenType: ACCESS 또는 REFRESH
		String tokenType = (String)claims.get(TokenClaimName.TYPE);
		if (!TokenType.ACCESS.equals(tokenType) && !TokenType.REFRESH.equals(tokenType)) {
			throw new BusinessException(INVALID_TOKEN_TYPE);
		}
	}
}
