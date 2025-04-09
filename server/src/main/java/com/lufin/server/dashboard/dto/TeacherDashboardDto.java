package com.lufin.server.dashboard.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeacherDashboardDto {
	// 상단 통계 정보
	private StatisticsDto statistics;

	// 학생 목록 정보
	private List<StudentItemDto> students;

	@Getter
	@Builder
	public static class StatisticsDto {
		private AssetDto deposit;     // 우리반 총 자산
		private AssetDto investment;  // 우리반 투자
		private AssetDto loan;        // 우리반 대출
	}

	@Getter
	@Builder
	public static class StudentItemDto {
		private Integer id;           // 학생 ID
		private String name;          // 학생 이름
		private Integer cash;         // 자산
		private Integer investment;   // 투자
		private Integer loan;         // 대출
		private String creditGrade;   // 신용등급
		private String missionStatus; // 미션 상태 (검토 필요, 수행 중, 수행 완료)
		private String loanStatus;    // 대출 상태 (검토 필요, 승인, 거절)
		private int items;    		  // 아이템 수량
	}
}
