package com.lufin.server.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class MaliciousRequestFilter implements Filter {

	private static final List<String> BLOCKED_PATHS = List.of(
		"/.git", "/.env", "/phpunit", "/cgi-bin", "/vendor", "/containers", "/eval-stdin", "/call_user_func_array"
	);

	private static final List<String> BLOCKED_AGENTS = List.of(
		"zgrab", "CensysInspect", "l9explore", "Go-http-client", "Python", "curl", "Puffin", "scaninfo"
	);

	private static final List<String> SAFE_AGENTS = List.of(
		"Mozilla", "Chrome", "Safari", "Edge", "Firefox", "SamsungBrowser", "Opera"
	);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String uri = req.getRequestURI().toLowerCase();
		String userAgent = req.getHeader("User-Agent");

		// 정상 브라우저는 무조건 통과 (화이트리스트 우선)
		if (userAgent != null && SAFE_AGENTS.stream().anyMatch(userAgent::contains)) {
			chain.doFilter(request, response);
			return;
		}

		// 경로 기반 차단
		boolean blockedPath = BLOCKED_PATHS.stream().anyMatch(uri::contains);

		// User-Agent 기반 차단
		boolean blockedAgent = userAgent != null &&
			BLOCKED_AGENTS.stream().anyMatch(agent -> userAgent.toLowerCase().contains(agent.toLowerCase()));

		if (blockedPath || blockedAgent) {
			log.warn("[차단된 요청]: URI={}, User-Agent={}", uri, userAgent);
			res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
			return;
		}

		chain.doFilter(request, response);
	}
}
