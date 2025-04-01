package com.lufin.server.classroom.service.impl;

import static com.lufin.server.classroom.util.ClassroomValidator.*;
import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.dto.ClassCodeResponse;
import com.lufin.server.classroom.dto.FindClassesResponse;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.classroom.service.ClassroomQueryService;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassroomQueryServiceImpl implements ClassroomQueryService {

	private final MemberClassroomRepository memberClassroomRepository;

	@Transactional(readOnly = true)
	@Override
	public List<FindClassesResponse> findClasses(int memberId) {
		log.info("[클래스 이력 조회 요청] memberId: {}", memberId);

		// 특정 회원이 소속된 모든 학급 이력 조회
		List<MemberClassroom> memberClassrooms = memberClassroomRepository.findByMember_Id(memberId);
		log.debug("[조회된 클래스 수] {}", memberClassrooms.size());

		// 각 학급 정보를 DTO로 변환하여 반환
		return memberClassrooms.stream()
			.map(mc -> {
				Classroom classroom = mc.getClassroom();

				// 현재 학급에 속한 전체 인원 수 조회
				int memberCount = memberClassroomRepository.countByClassroom_Id(classroom.getId());

				// 현재 학급

				return new FindClassesResponse(
					classroom.getName(),
					classroom.getSchool(),
					classroom.getCreatedAt().getYear(),
					classroom.getGrade(),
					classroom.getClassGroup(),
					memberCount,
					classroom.getThumbnailKey()
				);
			})
			.toList(); // 스트림을 리스트로 변환하여 반환
	}

	@Transactional(readOnly = true)
	@Override
	public FindClassesResponse findCurrentClass(int memberId) {
		log.info("[현재 소속 클래스 조회 요청] memberId: {}", memberId);

		Optional<MemberClassroom> currentClassroom =
			memberClassroomRepository.findByMember_IdAndIsCurrentTrue(memberId);

		if (currentClassroom.isPresent()) {
			Classroom classroom = currentClassroom.get().getClassroom();
			int memberCount = memberClassroomRepository.countByClassroom_Id(classroom.getId());

			log.info("[현재 클래스 조회 성공] classId: {}, className: {}", classroom.getId(), classroom.getName());

			return new FindClassesResponse(
				classroom.getName(),
				classroom.getSchool(),
				classroom.getCreatedAt().getYear(),
				classroom.getGrade(),
				classroom.getClassGroup(),
				memberCount,
				classroom.getThumbnailKey()
			);
		}
		log.warn("🏫[현재 소속 클래스 없음] memberId: {}", memberId);
		throw new BusinessException(CLASS_NOT_FOUND);
	}

	@Transactional(readOnly = true)
	@Override
	public ClassCodeResponse findClassCode(Member teacher) {
		log.info("[클래스 코드 조회 요청] teacher: {}", teacher);

		Member currentMember = validateTeacherRole(teacher);

		return memberClassroomRepository
			.findByMember_IdAndIsCurrentTrue(currentMember.getId())
			.map(memberClassroom -> new ClassCodeResponse(memberClassroom.getClassroom().getCode()))
			.orElseThrow(() -> {
				log.warn("🏫[현재 소속된 클래스 없음]: {}", teacher);
				return new BusinessException(CLASS_NOT_FOUND);
			});
	}
}
