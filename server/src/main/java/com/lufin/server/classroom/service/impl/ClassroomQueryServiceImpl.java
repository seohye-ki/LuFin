package com.lufin.server.classroom.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.dto.ClassCodeResponse;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.classroom.service.ClassroomQueryService;
import com.lufin.server.common.annotation.TeacherOnly;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassroomQueryServiceImpl implements ClassroomQueryService {

	private final MemberClassroomRepository memberClassroomRepository;
	private final AccountRepository accountRepository;

	@Transactional(readOnly = true)
	@Override
	public List<ClassResponse> findClasses(int memberId) {
		log.info("[클래스 이력 조회] memberId: {}", memberId);

		List<MemberClassroom> memberClassrooms = memberClassroomRepository.findByMember_Id(memberId);
		log.debug("[조회된 클래스 수] {}", memberClassrooms.size());

		return memberClassrooms.stream()
			.map(mc -> toClassResponse(mc.getClassroom()))
			.toList();
	}

	@Transactional(readOnly = true)
	@Override
	public ClassResponse findCurrentClass(int memberId) {
		log.info("[현재 클래스 조회] memberId: {}", memberId);

		Classroom classroom = memberClassroomRepository.findByMember_IdAndIsCurrentTrue(memberId)
			.map(MemberClassroom::getClassroom)
			.orElseThrow(() -> {
				log.warn("🏫[현재 소속 클래스 없음] memberId: {}", memberId);
				return new BusinessException(CLASS_NOT_FOUND);
			});

		log.info("[조회 성공] classId: {}, className: {}", classroom.getId(), classroom.getName());
		return toClassResponse(classroom);
	}

	@Transactional(readOnly = true)
	@TeacherOnly
	@Override
	public ClassCodeResponse findClassCode(Member teacher) {
		log.info("[클래스 코드 조회] teacher: {}", teacher);

		String code = memberClassroomRepository.findByMember_IdAndIsCurrentTrue(teacher.getId())
			.map(mc -> mc.getClassroom().getCode())
			.orElseThrow(() -> {
				log.warn("🏫[현재 클래스 없음] teacher: {}", teacher);
				return new BusinessException(CLASS_NOT_FOUND);
			});

		return new ClassCodeResponse(code);
	}

	private ClassResponse toClassResponse(Classroom classroom) {
		Integer balance = accountRepository.findByClassroomIdAndMemberIdIsNull(classroom.getId())
			.map(Account::getBalance)
			.orElse(0);

		return new ClassResponse(
			classroom.getId(),
			classroom.getName(),
			classroom.getSchool(),
			classroom.getCreatedAt().getYear(),
			classroom.getGrade(),
			classroom.getClassGroup(),
			classroom.getCode(),
			classroom.getMemberCount(),
			balance,
			classroom.getThumbnailKey()
		);
	}
}
