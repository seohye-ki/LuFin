package com.lufin.server.member.dto;

public record LoginResponse(String accessToken,
							String refreshToken,
							String role) {
}
