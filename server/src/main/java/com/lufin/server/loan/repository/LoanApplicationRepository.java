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

	boolean existsByMemberAndClassroomAndStatusIn(Member member, Classroom classroom, List<LoanApplicationStatus> statuses);
}
