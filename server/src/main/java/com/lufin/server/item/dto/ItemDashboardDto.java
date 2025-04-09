package com.lufin.server.item.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDashboardDto {
	private String name;		// 아이템 명
	private int quantity;		// 수량
	private int expireInDays;	// 만료일
}
