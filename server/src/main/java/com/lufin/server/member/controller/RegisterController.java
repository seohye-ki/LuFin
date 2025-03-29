package com.lufin.server.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.member.dto.RegisterRequest;
import com.lufin.server.member.dto.RegisterResponse;
import com.lufin.server.member.service.RegisterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lufin/register")
public class RegisterController {

	private final RegisterService registerService;

	@PostMapping
	public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {

		RegisterResponse response = registerService.register(request);

		return ResponseEntity.status(201).body(ApiResponse.success(response));
	}
}
