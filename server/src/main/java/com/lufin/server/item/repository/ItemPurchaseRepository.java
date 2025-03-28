package com.lufin.server.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lufin.server.item.domain.ItemPurchase;
import com.lufin.server.item.domain.ItemPurchaseStatus;

@Repository
public interface ItemPurchaseRepository extends JpaRepository<ItemPurchase, Integer> {
	// 인벤토리 조회
	@Query("SELECT ip FROM ItemPurchase ip "
		+ "JOIN FETCH ip.item i "
		+ "WHERE ip.member.id = :memberId "
		+ "AND ip.status = :status "
		+ "AND i.classroom.id = :classroomId")
	List<ItemPurchase> findInventory(@Param("memberId") Integer memberId,
		@Param("status") ItemPurchaseStatus status,
		@Param("classroomId") Integer classroomId);

	List<ItemPurchase> findByItemId(Integer itemId);

	List<ItemPurchase> findAllByStatus(ItemPurchaseStatus status);
}
