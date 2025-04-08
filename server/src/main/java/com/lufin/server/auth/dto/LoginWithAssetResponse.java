package com.lufin.server.auth.dto;

public record LoginWithAssetResponse(
	String accessToken,
	String refreshToken,
	String role,
	int classId,
	String name,
	String profileImage,
	int totalAsset
) {
	public static LoginWithAssetResponse of(
		String accessToken,
		String refreshToken,
		String role,
		int classId,
		String name,
		String profileImage,
		int totalAsset
	) {
		return new LoginWithAssetResponse(accessToken, refreshToken, role, classId, name, profileImage, totalAsset);
	}
}
