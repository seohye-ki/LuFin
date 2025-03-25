package com.lufin.server.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lufin.server.item.domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

}
