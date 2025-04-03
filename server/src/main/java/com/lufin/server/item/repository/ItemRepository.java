package com.lufin.server.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lufin.server.item.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findByClassroomId(Integer id);

	List<Item> findByClassroomIdAndStatusTrue(Integer classroomId);

	List<Item> findByStatusTrue();
}
