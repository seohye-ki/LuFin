package com.lufin.server.mission.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionUtilRepository {
	// (선생님) 미션 삭제
	@Query(value = "DELETE FROM mission WHERE id = :missionId AND class_id = :classId", nativeQuery = true)
	void deleteMission(Integer classId, Integer missionId);

	// 미션의 classId 조회
	@Query(value = "SELECT class_id FROM mission WHERE id = :missionId", nativeQuery = true)
	Integer getClassIdByMissionId(Integer missionId);

	// 미션 수정은 JPA 더티 체킹으로 시행

}
