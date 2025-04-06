package com.lufin.server.item.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lufin.server.item.domain.Item;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findByClassroomId(Integer id);

	List<Item> findByClassroomIdAndStatusTrue(Integer classroomId);

	List<Item> findByStatusTrue();

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({
		@QueryHint(name = "jakarta.persistence.lock.timeout", value = "1000")
	})
	@Query("SELECT i FROM Item i WHERE i.id = :itemId")
	Optional<Item> findByIdWithPessimisticLock(@Param("itemId") Integer itemId);
}
