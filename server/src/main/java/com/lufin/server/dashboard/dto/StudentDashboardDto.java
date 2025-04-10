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
	private int myMemberId;                         // 학생Id
	private String profileImage;                    // 프로필 이미지
	private List<RankingDto> rankings;              // 자산 랭킹 정보 (상위 5명)

	private String creditGrade;                     // 현재 신용 등급
	private int creditScore;                        // 현재 신용 점수
	private List<CreditHistoryDto> creditHistories; // 신용 점수 변화 이력 (점수 변화, 사유, 날짜 등)

	private int totalAsset;                         // 총 자산 (현금 + 주식 - 대출)
	private int cash;                               // 보유 현금
	private int stock;                              // 보유 주식 총액
	private int loan;                               // 대출 원금

	private AssetDto investmentStat;                // 투자 통계 (어제 대비 투자 금액 변화율)
	private AssetDto consumptionStat;               // 소비 통계 (이번 주 소비 금액 및 전주 대비 변화율)

	private List<ItemDashboardDto> items;           // 보유 아이템 목록 (아이템 명, 수량, 만료일)

	private List<MyMissionDto> ongoingMissions;     // 진행 중인 미션 목록 (참여 미션 정보)
	private int totalCompletedMissions;             // 완료한 미션 개수
	private int totalWage;                            // 누적 보상 총액
}
