package com.lufin.server.auth.dto;

public record TokenResponse(String accessToken,
							String refreshToken,
							String role,
							int classId,
							String accountNumber) {
}
