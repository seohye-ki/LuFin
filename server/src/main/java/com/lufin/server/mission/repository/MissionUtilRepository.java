package com.lufin.server.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lufin.server.mission.domain.Mission;

@Repository
public interface MissionUtilRepository extends JpaRepository<Mission, Integer> {
	// (선생님) 미션 삭제
	@Modifying
	@Query(value = "DELETE FROM missions WHERE mission_id = :missionId AND classroom_id = :classroomId", nativeQuery = true)
	void deleteMission(Integer classroomId, Integer missionId);

	// 미션의 classId 조회
	@Query(value = "SELECT classroom_id FROM missions WHERE mission_id = :missionId", nativeQuery = true)
	Integer getClassIdByMissionId(Integer missionId);

	// 미션 수정은 JPA 더티 체킹으로 시행

}
