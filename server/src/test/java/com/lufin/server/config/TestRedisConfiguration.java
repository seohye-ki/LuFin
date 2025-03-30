package com.lufin.server.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@TestConfiguration
@Profile("test")
public class TestRedisConfiguration {

	@Bean
	@Primary
	public RedisConnectionFactory redisConnectionFactory() {
		// Mock the Redis connection factory for tests
		return Mockito.mock(LettuceConnectionFactory.class);
	}
}
