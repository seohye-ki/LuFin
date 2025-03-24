package com.lufin.server.classroom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lufin.server.classroom.domain.MemberClassroom;

public interface MemberClassroomRepository extends JpaRepository<MemberClassroom, Long> {

	// 특정 회원의 현재 소속 학급
	@Query("""
		SELECT m from MemberClassroom m WHERE m.member.id = :memberId AND m.isCurrent = true
		""")
	Optional<MemberClassroom> findByMember_IdAndIsCurrentTrue(Integer memberId);

	// 특정 학급에 속한 구성원 전체 조회
	List<MemberClassroom> findByClassroom_Id(Integer classroomId);

	// 현재 학급 구성원 수 조회
	@Query("""
		SELECT COUNT(m) FROM MemberClassroom m WHERE m.classroom.id = :classroomId AND m.isCurrent=true
		""")
	int countByClassroom_IdAndMemberIsCurrentTrue(Integer classroomId);

	// 특정 회원이 특정 학급에 소속되었는지 확인 (중복 등록 방지)
	boolean existsByMember_IdAndClassroom_Id(Integer memberId, Integer classroomId);
}
