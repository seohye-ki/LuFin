package com.lufin.server.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.mission.domain.MissionParticipation;

public interface missionParticipationRepository extends JpaRepository<MissionParticipation, Integer> {
}
