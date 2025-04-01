package com.lufin.server.classroom.dto;

public record TokenResponse(String accessToken,
							String refreshToken,
							String role,
							int classId,
							String accountNumber) {
}
