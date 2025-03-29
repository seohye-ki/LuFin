package com.lufin.server.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.member.dto.LoginRequest;
import com.lufin.server.member.dto.LoginResponse;
import com.lufin.server.member.service.LoginService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lufin/auth")
public class AuthController {

	private final LoginService loginService;

	@PostMapping("login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {

		LoginResponse response = loginService.login(request.email(), request.password());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-store");
		headers.add("Pragma", "no-cache");

		return ResponseEntity.ok().headers(headers).body(ApiResponse.success(response));
	}
}
