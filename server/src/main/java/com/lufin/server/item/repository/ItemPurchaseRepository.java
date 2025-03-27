package com.lufin.server.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lufin.server.item.domain.ItemPurchase;
import com.lufin.server.item.domain.ItemPurchaseStatus;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ItemPurchaseRepository extends JpaRepository<ItemPurchase, Integer> {
	// 인벤토리 조회
	@Query("SELECT ip FROM ItemPurchase ip JOIN ip.item i "
		+ "WHERE ip.member.id = :memberId "
		+ "AND ip.status = :status "
		+ "AND i.classroom.id = :classroomId")
	List<ItemPurchase> findInventory(@Param("memberId") Integer memberId,
		@Param("status") ItemPurchaseStatus status,
		@Param("classroomId") Integer classroomId);

	// 특정 아이템 구매 내역 조회
	List<ItemPurchase> findByItemId(Integer itemId);
}
