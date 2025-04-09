package com.lufin.server.auth.dto;

public record LoginAssetDto(
	int cash,
	int stock,
	int loan,
	int totalAsset
) {
	public static LoginAssetDto of(int cash, int stock, int loan) {
		return new LoginAssetDto(cash, stock, loan, cash + stock - loan);
	}
}
