package com.lufin.server.account.service;

import java.time.LocalDate;

import com.lufin.server.account.domain.Account;
import com.lufin.server.classroom.domain.Classroom;

public interface AccountService {

	// 클래스 code 치고 입장 시 학생 계좌 생성
	Account createAccountForMember(int memberId, Classroom classroom);

	// 클래스 생성시 클래스 계좌 생성 (교사 소유 X)
	Account createAccountForClassroom(Classroom classroom);

	// 현금 자산 확인
	int getCashBalance(int memberId, int classroomId);

	long getTotalClassDeposit(int classId, LocalDate date);
}
