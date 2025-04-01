package com.lufin.server.loan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.loan.domain.LoanApplication;
import com.lufin.server.loan.domain.LoanApplicationStatus;
import com.lufin.server.member.domain.Member;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Integer> {

	// 특정 학생의 특정 반에서의 활성 대출이 있는지 조회
	boolean existsByMemberAndClassroomAndStatusIn(Member member, Classroom classroom, List<LoanApplicationStatus> statuses);

	// 특정 반의 모든 대출 신청 조회
	List<LoanApplication> findByClassroom(Classroom classroom);

	// 특정 학생의 특정 반에서의 대출 신청 조회
	List<LoanApplication> findByMemberAndClassroom(Member member, Classroom classroom);
}
