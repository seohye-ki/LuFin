package com.lufin.server.mission.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.lufin.server.mission.domain.Mission;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer>, MissionRepositoryCustom {
	/**
	 * 비관적 락을 통한 missionId로 mission을 검색하는 쿼리
	 * 동시성 해결
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({
		@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")
	})
	@Query("SELECT m FROM Mission m WHERE m.id = :id AND m.classroom.id = :classId")
	Optional<Mission> findByIdAndClassIdWithPessimisticLock(@Param("id") Integer id, @Param("classId") Integer classId);

}
