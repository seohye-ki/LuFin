package com.lufin.server.member.dto;

import com.lufin.server.member.domain.MemberRole;

public record RegisterResponse(String email, MemberRole role) {
}
