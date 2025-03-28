package com.lufin.server.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lufin.server.mission.domain.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer>, MissionRepositoryCustom {
}
