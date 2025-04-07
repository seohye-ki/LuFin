package com.lufin.server.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetDto {
	private String label;         // 라벨명
	private Long amount;          // 금액
	private Integer changeRate;   // 변동률 (15%, -5% 등)
	private Boolean isPositive;   // 증가/감소 여부
}
