package com.lufin.server.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingDto {
	private int memberId;
	private String name;
	private int asset;
	private int rank;
}
