package com.lufin.server.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lufin.server.mission.domain.MissionParticipation;
import com.lufin.server.mission.domain.MissionParticipationStatus;
import com.lufin.server.mission.dto.MyMissionDto;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface MyMissionRepository extends JpaRepository<MissionParticipation, Integer> {

	// 해당 학생이 특정 반에서 참여한 미션 목록을 반환
	@Query(value =
		"SELECT new com.lufin.server.mission.dto.MyMissionDto(" +
			"m.id, mp.participationId, m.title, mp.status, m.wage, m.missionDate) " +
			"FROM MissionParticipation mp " +
			"JOIN mp.mission m " +
			"WHERE m.classroom.id = :classId AND mp.member.id = :memberId " +
			"ORDER BY mp.createdAt DESC")
	List<MyMissionDto> findMyMissions(@Param("classId") Integer classId, @Param("memberId") Integer memberId);

	// 특정 상태의 미션 참여 내역 조회
	List<MissionParticipation> findAllByMemberIdAndStatus(int memberId, MissionParticipationStatus status);

	// 특정 상태의 미션 참여 개수 반환
	int countByMemberIdAndStatus(int memberId, MissionParticipationStatus status);
}

