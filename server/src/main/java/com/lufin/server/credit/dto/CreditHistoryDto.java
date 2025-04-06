package com.lufin.server.credit.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditHistoryDto {
	private int scoreChange;
	private String reason;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate changedAt;
}
