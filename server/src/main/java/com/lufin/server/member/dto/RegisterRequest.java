package com.lufin.server.member.dto;

import com.lufin.server.member.domain.MemberRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(@NotBlank @Email String email,
							  @NotBlank String password,
							  @NotBlank String name,
							  @NotNull MemberRole role,
							  @Pattern(regexp = "^\\d{6}$") String secondaryPassword) {
}
