package com.lufin.server.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lufin.server.mission.domain.MissionParticipation;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

@Repository
public interface MissionParticipationRepository
	extends JpaRepository<MissionParticipation, Integer>, MissionParticipationRepositoryCustom {
	/**
	 * 비관적 락을 사용하여 특정 회원이 진행 중인 미션 참여가 있는지 확인합니다.
	 * EXISTS를 사용하여 성능을 최적화합니다.
	 *
	 * @param memberId 확인할 회원 ID
	 * @return 진행 중인 미션 참여 여부
	 */
	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT EXISTS ("
		+ "SELECT 1 FROM MissionParticipation mp "
		+ "WHERE mp.member.id = :memberId "
		+ "AND mp.mission.classroom.id = :classId "
		+ "AND mp.status = 'IN_PROGRESS')")
	boolean existsByMemberIdAndStatusInProgress(
		@Param("memberId") Integer memberId,
		@Param("classId") Integer classId
	);

}
