package com.lufin.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://j12a402.p.ssafy.io:5173", "http://localhost:5173")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS 포함
			.allowCredentials(true) // 쿠키 인증 허용
			.allowedHeaders("*") // 모든 헤더 허용
			.maxAge(3600); // preflight 요청 캐싱 시간
	}
}
