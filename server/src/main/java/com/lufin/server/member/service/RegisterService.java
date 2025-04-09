package com.lufin.server.member.service;

import com.lufin.server.member.dto.RegisterRequest;
import com.lufin.server.member.dto.RegisterResponse;

public interface RegisterService {

	RegisterResponse register(RegisterRequest registerRequest);

	void validateRegisterEmail(String inputEmail);
}
