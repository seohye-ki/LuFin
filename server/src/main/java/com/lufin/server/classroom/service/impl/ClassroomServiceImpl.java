package com.lufin.server.classroom.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.service.AccountService;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.dto.ClassCodeRequest;
import com.lufin.server.classroom.dto.ClassCodeResponse;
import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.classroom.dto.FindClassesResponse;
import com.lufin.server.classroom.dto.LoginWithClassResponse;
import com.lufin.server.classroom.dto.UpdateClassRequest;
import com.lufin.server.classroom.factory.ResponseFactory;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
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
	private final MemberClassroomRepository memberClassroomRepository;
	private final AccountService accountService;
	private final ResponseFactory responseFactory;

	@Transactional
	@Override
	public LoginWithClassResponse createClassroom(ClassRequest request, Member currentMember) {
		Member teacher = memberAuthorization(currentMember);

		// 동일한 교사가 같은 해, 같은 학교, 같은 학년, 같은 반 번호로 클래스 생성 시 중복
		checkDuplicateClassroom(request.school(), request.grade(), request.classGroup(), teacher);

		// 클래스 코드 생성
		String classCode = generateUniqueClassCode();

		Classroom newClass = Classroom.create(
			teacher,
			classCode,
			request.school(),
			request.grade(),
			request.classGroup(),
			request.name(),
			request.key()
		);

		classroomRepository.save(newClass);

		// 클래스 계좌 생성
		Account account = accountService.createAccountForClassroom(newClass);

		// 기존에 소속된 클래스(isCurrent=true)가 있다면 deactivate()
		deactivateIfInActiveClass(teacher);

		// 교사를 클래스에 매핑
		MemberClassroom addTeacher = MemberClassroom.enroll(teacher, newClass);
		memberClassroomRepository.save(addTeacher);

		return responseFactory.createLoginWithClassResponse(teacher, newClass, account);
	}

	@Transactional(readOnly = true)
	@Override
	public List<FindClassesResponse> findClasses(int memberId) {

		// 특정 회원이 소속된 모든 학급 이력 조회
		List<MemberClassroom> memberClassrooms = memberClassroomRepository.findByMember_Id(memberId);

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

		Optional<MemberClassroom> currentClassroom =
			memberClassroomRepository.findByMember_IdAndIsCurrentTrue(memberId);

		if (currentClassroom.isPresent()) {
			Classroom classroom = currentClassroom.get().getClassroom();
			int memberCount = memberClassroomRepository.countByClassroom_Id(classroom.getId());

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
		throw new BusinessException(CLASS_NOT_FOUND);
	}

	@Transactional(readOnly = true)
	@Override
	public ClassCodeResponse findClassCode(Member teacher) {

		Member currentMember = memberAuthorization(teacher);

		return memberClassroomRepository
			.findByMember_IdAndIsCurrentTrue(currentMember.getId())
			.map(memberClassroom -> new ClassCodeResponse(memberClassroom.getClassroom().getCode()))
			.orElseThrow(() -> new BusinessException(CLASS_NOT_FOUND));
	}

	@Transactional
	@Override
	public LoginWithClassResponse enrollClass(Member member, ClassCodeRequest request) {

		// 코드로 조회되는 반이 있나요?
		Classroom classroom = classroomRepository.findByCode(request.code())
			.orElseThrow(() -> new BusinessException(CLASS_NOT_FOUND));

		// 기존에 소속된 클래스(isCurrent=true)가 있다면 deactivate()
		deactivateIfInActiveClass(member);

		// 클래스에 멤버 저장 후 계좌 개설
		MemberClassroom.enroll(member, classroom);
		Account account = accountService.createAccountForMember(member.getId());

		// 토큰 발급
		return responseFactory.createLoginWithClassResponse(member, classroom, account);
	}

	@Transactional
	@Override
	public ClassResponse updateClassroom(Member member, UpdateClassRequest request) {

		Member teacher = memberAuthorization(member);

		// 현재 소속 클래스 조회
		MemberClassroom current = memberClassroomRepository
			.findByMember_IdAndIsCurrentTrue(teacher.getId())
			.orElseThrow(() -> new BusinessException(CLASS_NOT_FOUND));

		Classroom classroom = current.getClassroom();

		// 수정하려는 정보로 중복 체크 (학교, 학년, 반 번호 + 연도 + 교사)
		checkDuplicateClassroom(request.school(), request.grade(), request.classGroup(), teacher);

		// 엔티티 내부에서 수정 메서드 호출
		classroom.updateInfo(
			request.school(),
			request.grade(),
			request.classGroup(),
			request.name(),
			request.key()
		);

		Account account = accountService.createAccountForMember(member.getId());

		return new ClassResponse(
			classroom.getId(),
			classroom.getName(),
			classroom.getSchool(),
			classroom.getCreatedAt().getYear(),
			classroom.getGrade(),
			classroom.getClassGroup(),
			classroom.getCode(),
			account.getBalance()
		);
	}

	private void checkDuplicateClassroom(String school, int grade, int classGroup, Member teacher) {
		int year = LocalDate.now().getYear();
		boolean exist = classroomRepository.existsDuplicateClassroom(school, grade,
			classGroup, year, teacher.getId());
		if (exist) {
			throw new BusinessException(DUPLICATE_CLASSROOM);
		}
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

	private void deactivateIfInActiveClass(Member teacher) {
		Optional<MemberClassroom> hasCurrentClassroom = memberClassroomRepository.findByMember_IdAndIsCurrentTrue(
			teacher.getId());

		if (hasCurrentClassroom.isPresent()) {
			hasCurrentClassroom.get().deactivate();
			memberClassroomRepository.save(hasCurrentClassroom.get());
		}
	}
}
