package com.lufin.server.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lufin.server.common.filter.MaliciousRequestFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<MaliciousRequestFilter> maliciousRequestFilter() {
		FilterRegistrationBean<MaliciousRequestFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new MaliciousRequestFilter());
		registrationBean.addUrlPatterns("/*"); // 모든 요청에 적용
		registrationBean.setOrder(1); // 우선순위 높게 설정
		return registrationBean;
	}
}
