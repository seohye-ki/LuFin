package com.lufin.server.item.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDashboardDto {
	private String name;
	private int quantity;
	private int expireInDays;
}
