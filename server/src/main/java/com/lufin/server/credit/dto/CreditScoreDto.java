package com.lufin.server.credit.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lufin.server.credit.domain.CreditScore;

public record CreditScoreDto(
	Integer memberId,
	Byte score,
	String grade,
	Byte creditStatus,
	String creditStatusDescription,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime createdAt,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime updatedAt
) {
	public static CreditScoreDto from(CreditScore creditScore) {
		return new CreditScoreDto(
			creditScore.getMemberId(),
			creditScore.getScore(),
			creditScore.getGrade().getDisplay(),
			creditScore.getCreditStatus(),
			creditScore.getCreditStatusDescription(),
			creditScore.getCreatedAt(),
			creditScore.getUpdatedAt()
		);
	}
}
