package com.lufin.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.lufin.server.config.TestRedisConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestRedisConfiguration.class)
class ServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
