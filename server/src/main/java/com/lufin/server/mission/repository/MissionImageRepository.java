package com.lufin.server.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.mission.domain.MissionImage;

public interface MissionImageRepository extends JpaRepository<MissionImage, Integer> {
}
