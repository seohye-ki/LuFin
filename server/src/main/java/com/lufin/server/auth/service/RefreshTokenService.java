package com.lufin.server.auth.service;

import com.lufin.server.auth.dto.TokenResponse;

public interface RefreshTokenService {

	TokenResponse reissueAccessToken(String refreshToken);
}
