package com.lufin.server.mission.service;

import java.util.List;

import com.lufin.server.mission.dto.MyMissionDto;

public interface MyMissionService {

	// 학생의 미션 지원 내역
	List<MyMissionDto> getMyMissions(int classId, int memberId);

	// 완료된 미션 수
	int getCompletedCount(int memberId);

	// 완료한 미션의 누적 보상 금액 총합
	int getTotalWage(int memberId);
}
