package com.lufin.server.classroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lufin.server.classroom.domain.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

	// 고유 코드로 조회 (중복 방지에 사용)
	Optional<Classroom> findByCode(String code);

	// 교사 ID로 현재 반 목록 조회 (필요 시)
	List<Classroom> findByTeacher_Id(Integer teacherId);
}
