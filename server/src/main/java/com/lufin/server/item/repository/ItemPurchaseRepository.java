package com.lufin.server.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lufin.server.item.domain.ItemPurchase;

@Repository
public interface ItemPurchaseRepository extends JpaRepository<ItemPurchase, Integer> {
	// 특정 회원의 구매 내역 조회
	List<ItemPurchase> findByMemberId(Integer memberId);

	// 특정 아이템 구매 내역 조회
	List<ItemPurchase> findByItemId(Integer itemId);
}
