package com.lufin.server.auth.dto;

public record LoginWithAssetResponse(
	String accessToken,
	String refreshToken,
	String role,
	int classId,
	String name,
	String profileImage,
	int totalAsset,
	int memberId
) {
	public static LoginWithAssetResponse of(
		String accessToken,
		String refreshToken,
		String role,
		int classId,
		String name,
		String profileImage,
		int totalAsset,
		int memberId
	) {
		return new LoginWithAssetResponse(accessToken, refreshToken, role, classId, name, profileImage, totalAsset,
			memberId);
	}
}
