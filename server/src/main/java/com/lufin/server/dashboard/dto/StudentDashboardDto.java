package com.lufin.server.dashboard.dto;

import java.util.List;

import com.lufin.server.credit.dto.CreditHistoryDto;
import com.lufin.server.item.dto.ItemDashboardDto;
import com.lufin.server.member.dto.RankingDto;
import com.lufin.server.mission.dto.MyMissionDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentDashboardDto {
	private List<RankingDto> rankings;

	private String creditGrade;
	private int creditScore;
	private List<CreditHistoryDto> creditHistories;

	private int totalAsset;
	private int cash;
	private int stock;
	private int loan;

	private AssetDto investmentStat;
	private AssetDto consumptionStat;

	private int loanPrincipal;

	private List<ItemDashboardDto> items;

	private List<MyMissionDto> ongoingMissions;
	private int totalCompletedMissions;
	private int totalReward;
}
