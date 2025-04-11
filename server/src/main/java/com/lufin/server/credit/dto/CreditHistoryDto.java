package com.lufin.server.credit.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditHistoryDto {
	private int scoreChange;              // 신용 점수 변화량 (예: +10, -5 등)
	private String reason;                // 점수 변동 사유 (대출 원금 상환, 대출 원금 연체, 대출 이자 상환, 대출 이자 연체, 미션 수행 완료, 미션 수행 실패, 회생 시스템)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate changedAt;          // 점수 변경 날짜 (yyyy-MM-dd 형식)
}
