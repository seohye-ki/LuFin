package com.lufin.server.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(@NotBlank(message = "이메일은 필수 입력값입니다")
						   String email,
						   @NotBlank(message = "비밀번호는 필수 입력값입니다")
						   @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
						   String password) {
}
