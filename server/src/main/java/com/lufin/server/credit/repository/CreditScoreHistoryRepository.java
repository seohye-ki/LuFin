package com.lufin.server.credit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.credit.domain.CreditScoreHistory;

public interface CreditScoreHistoryRepository extends JpaRepository<CreditScoreHistory, Integer> {

	List<CreditScoreHistory> findTop10ByMemberClassroomOrderByCreatedAtDesc(MemberClassroom memberClassroom);

}
