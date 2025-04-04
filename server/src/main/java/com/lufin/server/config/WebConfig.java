package com.lufin.server.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
			.excludePathPatterns("/api/v1/lufin/auth/login", "/api/v1/lufin/register/**",
				"/api/v1/lufin/auth/refresh-token",
				"/css/**", "/js/**", "/favicon.ico",
				"/api/test-login", "/app/**", "/ws/**"); // 예외 경로
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("https://j12a402.p.ssafy.io:5173", "http://j12a402.p.ssafy.io:5173",
				"http://localhost:5173", "http://127.0.0.1:5173")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS 포함
			.allowCredentials(true) // 쿠키 인증 허용
			.allowedHeaders("*") // 모든 헤더 허용
			.maxAge(3600); // preflight 요청 캐싱 시간
	}

	// JSON 응답의 기본 문자셋을 UTF-8로 강제 설정
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setDefaultCharset(StandardCharsets.UTF_8);
		return converter;
	}
}
