package com.lufin.server.classroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lufin.server.classroom.domain.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

	// 고유 코드로 조회 (중복 방지에 사용)
	Optional<Classroom> findByCode(String code);

	// 교사 ID로 현재 반 목록 조회 (필요 시)
	List<Classroom> findByTeacher_Id(Integer teacherId);

	@Query("""
		SELECT COUNT(c) > 0 FROM Classroom c
			WHERE c.school = :school
			AND c.grade = :grade
			AND c.classGroup = :classGroup
			AND YEAR(c.createdAt) = :year
			AND c.teacher.id = :teacherId""")
	boolean existsDuplicateClassroom(@Param("school") String school,
		@Param("grade") int grade,
		@Param("classGroup") int classGroup,
		@Param("year") int year,
		@Param("teacherId") int teacherId);

	// 클래스 ID와 교사 ID로 클래스 찾기
	Optional<Classroom> findByIdAndTeacher_Id(int classId, int teacherId);
}
