package com.lufin.server.websocket.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.lufin.server.common.utils.TokenUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

	private final TokenUtils tokenUtils;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
		WebSocketHandler wsHandler, Map<String, Object> attributes) {
		if (request instanceof ServletServerHttpRequest servletRequest) {
			HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
			String token = httpServletRequest.getParameter("token");

			if (token != null) {
				try {
					Claims claims = tokenUtils.extractClaims(token);
					attributes.put("userId", Integer.parseInt(claims.getSubject()));
					attributes.put("role", claims.get("role"));

					// classId 값이 있는 경우에만 Integer로 변환하여 저장
					Object classIdObj = claims.get("classId");
					if (classIdObj != null) {
						try {
							Integer classId = null;
							if (classIdObj instanceof Integer) {
								classId = (Integer)classIdObj;
							} else if (classIdObj instanceof String) {
								classId = Integer.parseInt((String)classIdObj);
							} else if (classIdObj instanceof Number) {
								classId = ((Number)classIdObj).intValue();
							}

							if (classId != null) {
								attributes.put("classId", classId);
							}
						} catch (Exception e) {
							log.warn("💡 ClassId 변환 실패: {}", e.getMessage());
						}
					}

					return true;
				} catch (Exception e) {
					log.warn("💡 WebSocket JWT 인증 실패: {}", e.getMessage());
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Exception exception) {

	}
}
