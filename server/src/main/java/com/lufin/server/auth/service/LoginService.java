package com.lufin.server.auth.service;

import com.lufin.server.member.dto.LoginResponse;

public interface LoginService {

	// 로그인
	LoginResponse login(String username, String password);
}
