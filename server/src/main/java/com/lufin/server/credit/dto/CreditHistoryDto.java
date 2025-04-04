package com.lufin.server.credit.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditHistoryDto {
	private int scoreChange;
	private String reason;
	private LocalDate changedAt;
}
