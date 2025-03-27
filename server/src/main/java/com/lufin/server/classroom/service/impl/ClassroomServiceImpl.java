package com.lufin.server.classroom.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.classroom.service.ClassroomService;
import com.lufin.server.classroom.util.ClassCodeGenerator;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

	private final ClassroomRepository classroomRepository;

	@Transactional
	@Override
	public ClassResponse createClassroom(ClassRequest request, Member currentMember) {
		Member teacher = memberAuthorization(currentMember);

		// 동일한 교사가 같은 해, 같은 학교, 같은 학년, 같은 반 번호로 클래스 생성 시 중복
		int year = LocalDate.now().getYear();

		boolean exist = classroomRepository.existsDuplicateClassroom(request.school(), request.grade(),
			request.classGroup(), year, teacher.getId());

		if (exist) {
			throw new BusinessException(DUPLICATE_CLASSROOM);
		}

		String classCode = generateUniqueClassCode();

		Classroom newClass = Classroom.create(
			teacher,
			classCode,
			request.school(),
			request.grade(),
			request.classGroup(),
			request.name(),
			request.fileName()
		);

		classroomRepository.save(newClass);

		return new ClassResponse(
			newClass.getId(),
			newClass.getName(),
			newClass.getSchool(),
			newClass.getCreatedAt().getYear(),
			newClass.getGrade(),
			newClass.getClassGroup(),
			newClass.getCode()
		);
	}

	private Member memberAuthorization(Member currentMember) {
		if (currentMember == null) {
			throw new BusinessException(UNAUTHORIZED_ACCESS);
		}
		if (currentMember.getMemberRole() != MemberRole.TEACHER) {
			throw new BusinessException(REQUEST_DENIED);
		}
		return currentMember;
	}

	private String generateUniqueClassCode() {
		String classCode;
		do {
			classCode = ClassCodeGenerator.generateClassCode();
		} while (classroomRepository.findByCode(classCode).isPresent());
		return classCode;
	}
}
