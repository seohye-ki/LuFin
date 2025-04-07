package com.lufin.server.credit.service;

import java.util.List;

import com.lufin.server.credit.dto.CreditHistoryDto;
import com.lufin.server.credit.dto.CreditScoreDto;
import com.lufin.server.member.domain.Member;

public interface CreditService {

	// 해당 학생의 신용 점수를 반환 (0~100)
	int getScore(int memberId, int classId);

	// 해당 학생의 신용 등급을 문자열로 반환 (예: "A+", "B")
	String getGrade(int memberId, int classId);

	// 해당 학생의 점수 변화 이력을 반환
	List<CreditHistoryDto> getGradeChangeHistory(int memberId, int classId);

	// 특정 학생 개인 회생 처리
	CreditScoreDto recoverCreditStatus(Member teacher, int memberId, int classId);
}
