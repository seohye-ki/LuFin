package com.lufin.server.classroom.dto;

import com.lufin.server.auth.dto.TokenResponse;

public record LoginWithClassResponse(TokenResponse token, ClassResponse classInfo) {
}
