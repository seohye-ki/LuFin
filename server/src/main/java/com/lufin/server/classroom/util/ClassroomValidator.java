package com.lufin.server.classroom.util;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;

public class ClassroomValidator {

	private static final Pattern CLASS_CODE_PATTERN = Pattern.compile("^[a-zA-Z0-9]{5}$");
	private static final Pattern FILE_NAME_PATTERN =
		Pattern.compile("^classrooms/[a-f0-9\\-]{36}\\.(png|jpg|jpeg)$", Pattern.CASE_INSENSITIVE);

	private ClassroomValidator() {
	}

	public static void validateClassCode(String inputCode) {
		if (!StringUtils.hasText(inputCode)) {
			throw new BusinessException(MISSING_REQUIRED_VALUE); // 클래스 코드 누락
		} else if (!CLASS_CODE_PATTERN.matcher(inputCode).matches()) {
			throw new BusinessException(CLASS_CODE_INVALID); // 형식 오류
		}

	}

	public static void validateThumbnailFileName(String inputFileName) {
		if (!StringUtils.hasText(inputFileName)) {
			throw new BusinessException(MISSING_REQUIRED_VALUE); // 클래스 코드 누락
		} else if (!FILE_NAME_PATTERN.matcher(inputFileName).matches()) {
			throw new BusinessException(INVALID_FILE_NAME_FORMAT); // 형식 오류
		}
	}

	public static void validateCreateClassroom(Member teacher, String inputCode, Integer inputGrade) {
		validateTeacherRole(teacher);
		validateClassCode(inputCode);
		validateGrade(inputGrade);
	}

	private static void validateTeacherRole(Member teacher) {
		if (teacher == null || teacher.getMemberRole() != MemberRole.TEACHER) {
			throw new BusinessException(REQUEST_DENIED); // 교사만 생성 가능
		}
	}

	private static void validateGrade(Integer inputGrade) {
		if (inputGrade == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE); // 학년 누락
		}
		if (inputGrade < 4 || inputGrade > 6) {
			throw new BusinessException(INVALID_INPUT_VALUE); // 잘못된 입력값
		}
	}
}
