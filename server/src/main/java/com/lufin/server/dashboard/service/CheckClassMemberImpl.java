package com.lufin.server.dashboard.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import org.springframework.stereotype.Service;

import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckClassMemberImpl implements ClassMembershipValidator {

	private final MemberClassroomRepository memberClassroomRepository;

	@Override
	public void validateTeacherAccessToStudent(Integer teacherId, Integer studentId, Integer classId) {
		if ((teacherId == null || studentId == null || classId == null) || (teacherId < 0 || studentId < 0
			|| classId < 0)) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		if (teacherId.equals(studentId)) {
			throw new BusinessException(FORBIDDEN_REQUEST);
		}

		boolean isStudent = memberClassroomRepository.existsByMember_IdAndClassroom_Id(studentId, classId);
		boolean isTeacher = memberClassroomRepository.existsByMember_IdAndClassroom_Id(teacherId, classId);

		if (!isStudent || !isTeacher) {
			throw new BusinessException(FORBIDDEN_REQUEST);
		}
	}

	public void validateStudentInClass(int studentId, int classId) {
		boolean isMember = memberClassroomRepository.existsByMember_IdAndClassroom_Id(studentId, classId);
		if (!isMember) {
			throw new BusinessException(FORBIDDEN_REQUEST);
		}
	}
}
