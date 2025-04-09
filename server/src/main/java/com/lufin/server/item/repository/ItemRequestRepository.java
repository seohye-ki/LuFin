package com.lufin.server.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lufin.server.item.domain.ItemRequest;
import com.lufin.server.item.domain.ItemRequestStatus;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
	@Query("SELECT r FROM ItemRequest r "
		+ "JOIN FETCH r.purchase p "
		+ "JOIN FETCH p.item i "
		+ "WHERE i.classroom.id = :classroomId "
		+ "AND r.status = :status "
		+ "ORDER BY r.createdAt DESC")
	List<ItemRequest> findByClassroomIdAndStatus(@Param("classroomId") Integer classroomId,
		@Param("status") ItemRequestStatus status);

	@Query("""
		SELECT r FROM ItemRequest r
		JOIN FETCH r.purchase p
		JOIN FETCH p.item i
		WHERE i.classroom.id = :classroomId
		AND p.member.id = :memberId
		ORDER BY r.createdAt DESC
		""")
	List<ItemRequest> findLatestByClassroomIdAndMemberId(@Param("classroomId") Integer classroomId,
		@Param("memberId") Integer memberId);

}
