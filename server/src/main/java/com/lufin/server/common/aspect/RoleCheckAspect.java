package com.lufin.server.common.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.member.support.UserContext;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RoleCheckAspect {
	// 교사 전용 메서드 실행 전 권한 확인
	@Before("@annotation(com.lufin.server.common.annotation.TeacherOnly)")
	public void checkTeacherRole() {
		Member member = getCurrentUser();
		if (member.getMemberRole() != MemberRole.TEACHER) {
			log.warn("🏫[권한 확인 실패] 교사 아님 - memberId: {}, role: {}", member.getId(), member.getMemberRole());
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		log.info("✅[권한 확인 성공] 교사 확인 완료 - memberId: {}", member.getId());
	}

	// 학생 전용 메서드 실행 전 권한 확인
	@Before("@annotation(com.lufin.server.common.annotation.StudentOnly)")
	public void checkStudentRole() {
		Member member = getCurrentUser();
		if (member.getMemberRole() != MemberRole.STUDENT) {
			log.warn("🎒[권한 확인 실패] 학생 아님 - memberId: {}, role: {}", member.getId(), member.getMemberRole());
			throw new BusinessException(ErrorCode.REQUEST_DENIED);
		}
		log.info("✅[권한 확인 성공] 학생 확인 완료 - memberId: {}", member.getId());
	}

	private Member getCurrentUser() {
		Member member = UserContext.get();
		if (member == null) {
			log.warn("🔍[권한 확인 실패] UserContext에 사용자 정보 없음");
			throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
		}
		return member;
	}
}
