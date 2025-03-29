package com.lufin.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lufin.server.common.interceptor.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final AuthInterceptor authInterceptor;

	public WebConfig(AuthInterceptor authInterceptor) {
		this.authInterceptor = authInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
			.addPathPatterns("/**") // 모든 요청에 적용
			.excludePathPatterns("/api/v1/lufin/auth/login", "/api/v1/lufin/register/**", "/css/**", "/js/**",
				"/favicon.ico",
				"/api/test-login", "/app/**", "/ws/**"); // 예외 경로
	}
}
