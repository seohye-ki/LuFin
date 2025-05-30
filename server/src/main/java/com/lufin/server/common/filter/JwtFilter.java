package com.lufin.server.common.filter;

import static com.lufin.server.common.constants.ErrorCode.*;
import static com.lufin.server.common.utils.ValidationUtils.*;

import java.io.IOException;

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
import lombok.extern.slf4j.Slf4j;

/**
 * JWT를 직접 파싱하여 userId, tokenType, classId 유효성을 검증하는 필터
 * classId는 서비스 계층에서 필요한 경우 직접 검증.
 */
@Slf4j
@Component
public class JwtFilter implements Filter {

	private final TokenUtils tokenUtils;

	public JwtFilter(TokenUtils tokenUtils) {
		this.tokenUtils = tokenUtils;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String requestUri = httpRequest.getRequestURI();
		String method = httpRequest.getMethod();

		log.info("[JwtFilter] 요청 uri: {} method: {}", requestUri, method);
		// OPTIONS 요청은 무조건 통과 (CORS preflight)
		if ("OPTIONS".equalsIgnoreCase(method)) {
			log.debug("[JwtFilter] OPTIONS 요청 → 인증 없이 통과");
			filterChain.doFilter(request, response);
			return;
		}

		// 필터 예외 경로는 바로 통과
		if (isExcluded(requestUri)) {
			log.info("[JwtFilter] 인증 제외 경로 → 통과: {}", requestUri);
			filterChain.doFilter(request, response);
			return;
		}

		try {
			// JWT 기반 헤더 검증
			log.info("[JwtFilter] JWT 검증 시작");
			validateToken(httpRequest);
			log.info("[JwtFilter] JWT 검증 완료 → 필터 통과");
			filterChain.doFilter(request, response);

		} catch (BusinessException e) {
			// 검증 실패 시 401 반환
			log.warn("⚠️[JwtFilter] JWT 인증 실패 - 에러코드: {} | 메시지: {}", e.getErrorCode(), e.getMessage());
			httpResponse.sendError(401, "인증이 필요합니다.");
		}
	}

	// userId, tokenType, classId 유효성 검증
	private void validateToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		log.debug("[JwtFilter] Authorization 헤더: {}", authorizationHeader);

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			log.warn("⚠️[JwtFilter] 잘못된 Authorization 헤더 형식 → 인증 실패");
			throw new BusinessException(INVALID_AUTH_HEADER);
		}

		// JWT에서 Claims 추출
		Claims claims = tokenUtils.extractClaims(authorizationHeader);

		String userId = claims.getSubject();
		validateIntegerId(userId);

		// tokenType: ACCESS 또는 REFRESH
		String tokenType = (String)claims.get(TokenClaimName.TYPE);
		if (!TokenType.ACCESS.equals(tokenType) && !TokenType.REFRESH.equals(tokenType)) {
			log.warn("⚠️[JwtFilter] 유효하지 않은 tokenType → 인증 실패: {}", tokenType);
			throw new BusinessException(INVALID_TOKEN_TYPE);
		}

		String classId = claims.get(TokenClaimName.CLASS_ID, String.class);
		if (classId != null) {
			log.debug("[JwtFilter] classId 추출: {}", classId);
			validateIntegerId(classId);
			request.setAttribute("classId", Integer.parseInt(classId));
		}
	}

	private boolean isExcluded(String uri) {
		return uri.startsWith("/api/v1/lufin/auth/login")
			|| uri.startsWith("/api/v1/lufin/register")
			|| uri.startsWith("/api/v1/lufin/auth/refresh-token")
			|| uri.startsWith("/ws")
			|| uri.startsWith("/app");
	}
}
