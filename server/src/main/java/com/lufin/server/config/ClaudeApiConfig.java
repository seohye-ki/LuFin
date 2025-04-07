package com.lufin.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "claude.api")
@Getter
@Setter
public class ClaudeApiConfig {
	private String key;
	private String url;
	private String model;
	private Integer maxTokens;
	private String version;
	private Double temperature;

	@Bean
	public WebClient claudeWebClient() {
		return WebClient.builder()
			.baseUrl(url)
			.defaultHeader("x-api-key", key)
			.defaultHeader("anthropic-version", version)
			.defaultHeader("content-type", "application/json")
			.build();
	}
}
