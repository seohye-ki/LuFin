package com.lufin.server.classroom.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;
import static com.lufin.server.credit.domain.CreditEventType.*;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.account.service.AccountService;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.dto.ClassCodeRequest;
import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.classroom.dto.LoginWithClassResponse;
import com.lufin.server.classroom.dto.UpdateClassRequest;
import com.lufin.server.classroom.factory.ResponseFactory;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.classroom.service.ClassroomCommandService;
import com.lufin.server.classroom.util.ClassCodeGenerator;
import com.lufin.server.common.annotation.StudentOnly;
import com.lufin.server.common.annotation.TeacherOnly;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.credit.service.CreditScoreService;
import com.lufin.server.member.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassroomCommandServiceImpl implements ClassroomCommandService {

	private final ClassroomRepository classroomRepository;
	private final MemberClassroomRepository memberClassroomRepository;
	private final AccountService accountService;
	private final AccountRepository accountRepository;
	private final ResponseFactory responseFactory;
	private final CreditScoreService creditScoreService;

	@Transactional
	@TeacherOnly
	@Override
	public LoginWithClassResponse createClassroom(ClassRequest request, Member currentMember) {

		log.info("[클래스 생성 시도] 요청자 ID: {}", currentMember.getId());

		// 동일한 교사가 같은 해, 같은 학교, 같은 학년, 같은 반 번호로 클래스 생성 시 중복
		checkDuplicateClassroom(request.school(), request.grade(), request.classGroup(), currentMember);

		// 클래스 코드 생성
		String classCode = generateUniqueClassCode();
		log.debug("[클래스 코드 생성] {}", classCode);

		Classroom newClass = Classroom.create(
			currentMember,
			classCode,
			request.school(),
			request.grade(),
			request.classGroup(),
			request.name(),
			request.key()
		);

		classroomRepository.save(newClass);
		log.info("[클래스 저장 완료] classId: {}, code: {}", newClass.getId(), newClass.getCode());

		// 클래스 계좌 생성
		Account account = accountService.createAccountForClassroom(newClass);
		log.info("[클래스 계좌 생성 완료] accountId: {}", account.getId());

		// 기존에 소속된 클래스(isCurrent=true)가 있다면 deactivate()
		deactivateIfInActiveClass(currentMember);

		// 교사를 클래스에 매핑
		MemberClassroom addTeacher = MemberClassroom.enroll(currentMember, newClass);

		memberClassroomRepository.save(addTeacher);
		log.info("[교사 클래스 매핑 완료] 교사ID: {}, 클래스: {}", currentMember.getId(), newClass.getName());

		return responseFactory.createLoginWithClassResponse(currentMember, newClass, account);
	}

	@Transactional
	@StudentOnly
	@Override
	public LoginWithClassResponse enrollClass(Member member, ClassCodeRequest request) {
		log.info("[클래스 등록 요청] memberId: {}, classCode: {}", member.getId(), request.code());

		// 코드로 조회되는 반이 있나요?
		Classroom classroom = classroomRepository.findByCode(request.code())
			.orElseThrow(() -> {
				log.warn("🏫[클래스 코드 미존재] code: {}", request.code());
				return new BusinessException(CLASS_NOT_FOUND);
			});

		// 동일한 반에 다시 들어오려한다면? -> 이미 등록된 클래스임을 안내
		boolean isExist = memberClassroomRepository.existsByMember_IdAndClassroom_Id(member.getId(), classroom.getId());
		if (isExist) {
			log.warn("🏫[동일한 클래스에 접근 요청 시도] memberId: {}, classId: {}", member.getId(), classroom.getId());
			throw new BusinessException(DUPLICATE_CLASSROOM);
		}

		// 기존에 소속된 클래스(isCurrent=true)가 있다면 deactivate()
		deactivateIfInActiveClass(member);

		// 새로운 class 계좌 생성
		MemberClassroom addStudent = MemberClassroom.enroll(member, classroom);

		// memberCount++
		classroom.addMemberClass(addStudent);

		memberClassroomRepository.save(addStudent);

		// 기본 신용등급 설정
		creditScoreService.applyScoreChange(member, 0, INIT, classroom.getId());

		log.info("[학생 클래스 매핑 완료] memberId: {}, classId: {}", member.getId(), classroom.getId());

		Account account = accountService.createAccountForMember(member.getId(), classroom);
		log.info("[학생 계좌 생성 완료] memberId: {}, accountId: {}", member.getId(), account.getId());

		// 토큰 발급
		return responseFactory.createLoginWithClassResponse(member, classroom, account);
	}

	@Transactional
	@TeacherOnly
	@Override
	public ClassResponse updateClassroom(Member member, UpdateClassRequest request) {
		log.info("[클래스 정보 수정 요청] 요청자 ID: {}", member.getId());

		// 현재 소속 클래스 조회
		MemberClassroom current = memberClassroomRepository
			.findByMember_IdAndIsCurrentTrue(member.getId())
			.orElseThrow(() -> {
				log.warn("🏫[수정 실패 - 소속 클래스 없음] teacherId: {}", member.getId());
				return new BusinessException(CLASS_NOT_FOUND);
			});

		Classroom classroom = current.getClassroom();

		// 엔티티 내부에서 수정 메서드 호출
		classroom.updateInfo(
			request.school(),
			request.grade(),
			request.classGroup(),
			request.name(),
			request.key()
		);
		log.info("[클래스 정보 수정 완료] classId: {}", classroom.getId());

		Account account = accountRepository.findByClassroomIdAndMemberIdIsNull(classroom.getId())
			.orElseThrow(() -> new BusinessException(CLASS_NOT_FOUND));

		return new ClassResponse(
			classroom.getId(),
			classroom.getName(),
			classroom.getSchool(),
			classroom.getCreatedAt().getYear(),
			classroom.getGrade(),
			classroom.getClassGroup(),
			classroom.getCode(),
			classroom.getMemberCount(),
			account.getBalance(),
			classroom.getThumbnailKey()
		);
	}

	@Transactional
	@TeacherOnly
	@Override
	public void deleteClassroom(Member member, int classId) {
		log.info("[클래스 삭제 시도] 요청자: {}, classId: {}", member.getId(), classId);

		// 교사가 해당 클래스에 소속되어 있는지 확인
		boolean existsClassroom = memberClassroomRepository
			.existsByMember_IdAndClassroom_Id(member.getId(), classId);
		if (!existsClassroom) {
			log.warn("🏫[삭제 실패 - 소속된 클래스 아님] teacherId: {}, classId: {}", member.getId(), classId);
			throw new BusinessException(CLASS_NOT_FOUND);
		}

		// 교사가 만든 클래스인지 확인
		Classroom classroom = classroomRepository.findByIdAndTeacher_Id(classId, member.getId())
			.orElseThrow(() -> {
				log.warn("🏫[삭제 실패 - 본인이 생성한 클래스 아님] classId: {}", classId);
				return new BusinessException(CLASS_NOT_FOUND);
			});

		// 다른 멤버가 존재하면 삭제 불가
		int memberCount = memberClassroomRepository.countByClassroom_Id(classroom.getId());
		if (memberCount > 0) {
			log.warn("🏫[삭제 실패 - 학생 존재] classId: {}, 멤버 수: {}", classId, memberCount);
			throw new BusinessException(CLASS_HAS_STUDENTS);
		}

		// 연관 관계 먼저 삭제 (MemberClassroom)
		memberClassroomRepository.deleteAllByClassroom(classroom);
		log.info("[멤버-클래스 관계 삭제 완료] classId: {}", classId);

		classroomRepository.delete(classroom);
		log.info("[클래스 삭제 완료] classId: {}", classId);
	}

	@Transactional
	@TeacherOnly
	@Override
	public LoginWithClassResponse changeClassroom(Member member, int classId) {
		log.info("[클래스 변경 요청] memberId: {}, targetClassId: {}", member.getId(), classId);

		// 타겟 클래스 존재 여부 확인
		Classroom newClassroom = classroomRepository.findById(classId)
			.orElseThrow(() -> {
				log.warn("🏫[클래스 변경 실패 - 대상 클래스 없음] classId: {}", classId);
				return new BusinessException(CLASS_NOT_FOUND);
			});

		// 해당 멤버가 대상 클래스에 소속되어 있는지 확인
		MemberClassroom target = memberClassroomRepository
			.findByMemberIdAndClassroomId(member.getId(), classId)
			.orElseThrow(() -> {
				log.warn("🏫[클래스 변경 실패 - 멤버 소속 아님] memberId: {}, classId: {}", member.getId(), classId);
				return new BusinessException(CLASS_NOT_FOUND);
			});

		// 계좌 먼저 조회
		Account account = accountRepository.findByClassroomIdAndMemberIdIsNull(classId)
			.orElseThrow(() -> {
				log.warn("🏫[클래스 변경 실패 - 클래스 계좌 없음] classId: {}", classId);
				return new BusinessException(ACCOUNT_NOT_FOUND);
			});

		// 현재 소속 클래스 확인
		Optional<MemberClassroom> currentClass = memberClassroomRepository.findByMember_IdAndIsCurrentTrue(
			member.getId());
		if (currentClass.map(c -> c.getClassroom().getId().equals(classId)).orElse(false)) {
			log.info("🏫[클래스 변경 요청 - 현재 클래스와 동일] memberId: {}, classId: {}", member.getId(), classId);
			return responseFactory.createLoginWithClassResponse(member, newClassroom, account);
		}

		// 기존 소속 클래스가 있다면 비활성화
		deactivateIfInActiveClass(member);

		// 대상 클래스의 MemberClassroom isCurrent 활성화
		target.activate();
		memberClassroomRepository.save(target);
		log.info("[클래스 변경 완료] memberId: {}, newClassId: {}", member.getId(), classId);

		return responseFactory.createLoginWithClassResponse(member, newClassroom, account);
	}

	private void checkDuplicateClassroom(String school, int grade, int classGroup, Member teacher) {
		int year = LocalDate.now().getYear();
		boolean exist = classroomRepository.existsDuplicateClassroom(school, grade,
			classGroup, year, teacher.getId());
		if (exist) {
			log.warn("🏫[클래스 중복 발생] 동일한 조건의 클래스 존재 - teacherId: {}, year: {}", teacher.getId(), year);
			throw new BusinessException(DUPLICATE_CLASSROOM);
		}
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
			MemberClassroom current = hasCurrentClassroom.get();

			current.deactivate();
			memberClassroomRepository.save(current);
		}
	}
}
