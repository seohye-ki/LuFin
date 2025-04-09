package com.lufin.server.auth.factory;

import org.springframework.stereotype.Component;

import com.lufin.server.auth.dto.LoginWithAssetResponse;
import com.lufin.server.member.domain.Member;

@Component
public class LoginResponseFactory {

	public LoginWithAssetResponse createLoginFlatResponse(Member member, int classId, String accessToken,
		String refreshToken, int totalAsset) {

		return LoginWithAssetResponse.of(
			accessToken,
			refreshToken,
			member.getMemberRole().name(),
			classId,
			member.getName(),
			member.getProfileImage(),
			totalAsset
		);
	}
}

