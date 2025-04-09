package com.lufin.server.dashboard.util;

import com.lufin.server.dashboard.dto.AssetDto;

public class AssetStatUtils {

	private AssetStatUtils() {
	}

	/**
	 * 투자/소비/예금 등 비교용 통계 DTO 생성
	 * @param label 항목명
	 * @param current 현재 금액
	 * @param previous 비교 기준 금액
	 * @param total 총 금액
	 */
	public static AssetDto buildAssetDto(String label, long current, long previous, long total) {
		long diff = current - previous;
		int rate = (previous == 0) ? 0 : (int)((double)diff / previous * 100);
		boolean isPositive = diff >= 0;

		return AssetDto.builder()
			.label(label)
			.amount(total)
			.changeRate(rate)
			.isPositive(isPositive)
			.build();
	}
}
