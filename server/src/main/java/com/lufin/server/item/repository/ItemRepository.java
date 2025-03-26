package com.lufin.server.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lufin.server.item.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	// 전체 조회
	List<Item> findByClassroomId(Integer id);

	// 판매중인 아이템 조회
	List<Item> findByClassroomIdAndStatusTrue(Integer classroomId);
}
