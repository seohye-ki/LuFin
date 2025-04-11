package com.lufin.server.auth.dto;

public record LoginResponse(String accessToken,
							String refreshToken,
							String role,
							int classId) {
}
