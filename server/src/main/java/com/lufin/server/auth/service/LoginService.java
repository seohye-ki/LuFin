package com.lufin.server.auth.service;

import com.lufin.server.auth.dto.LoginWithAssetResponse;

public interface LoginService {

	// 로그인
	LoginWithAssetResponse login(String username, String password);
}
