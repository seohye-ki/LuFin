package com.lufin.server.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void successTest() throws Exception {
        // Given
        String responseData = "테스트 데이터";
        ApiResponse<String> response = ApiResponse.success(responseData);

        // When
        String json = objectMapper.writeValueAsString(response);
        System.out.println(json);

        // Then
        assertThat(json).contains("\"isSuccess\":true");
        assertThat(json).contains("\"data\":\"테스트 데이터\"");
        assertThat(json).doesNotContain("\"code\":");
        assertThat(json).doesNotContain("\"message\":");
        assertThat(json).doesNotContain("\"success\":");
    }

    @Test
    void failureTest() throws Exception {
        // Given
        ApiResponse<Void> response = ApiResponse.failure(ErrorCode.INVALID_PASSWORD_FORMAT);

        // When
        String json = objectMapper.writeValueAsString(response);
        System.out.println(json);

        // Then
        assertThat(json).contains("\"isSuccess\":false");
        assertThat(json).contains("\"code\":\"E400002\"");
        assertThat(json).contains("\"message\":\"비밀번호는 최소 8자리 이상이어야 하며, 특수문자를 포함해야 합니다.\"");
        assertThat(json).doesNotContain("\"data\":");
        assertThat(json).doesNotContain("\"success\":");
    }
}
