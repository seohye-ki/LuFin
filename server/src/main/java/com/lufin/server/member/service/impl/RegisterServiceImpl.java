package com.lufin.server.member.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;
import static com.lufin.server.member.util.MemberValidator.*;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.member.dto.RegisterRequest;
import com.lufin.server.member.dto.RegisterResponse;
import com.lufin.server.member.repository.MemberRepository;
import com.lufin.server.member.service.RegisterService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

	private final MemberRepository memberRepository;
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional
	@Override
	public RegisterResponse register(RegisterRequest request) {

		// 이메일 검증 하고왔나요?
		String redisEmail = redisTemplate.opsForValue().get("email" + request.email());
		if (redisEmail == null) {
			throw new BusinessException(UNAUTHORIZED_ACCESS);
		}
		// 비밀번호 유효성 검증
		isValidPassword(request.password());

		// 2차 비밀번호 유효성 검증
		isValidSecondaryPassword(request.secondaryPassword());

		// 객체 생성
		Member member;
		if (request.role().equals(MemberRole.STUDENT)) {
			member = Member.createStudent(
				request.email(),
				request.name(),
				request.password(),
				request.secondaryPassword());
		} else if (request.role().equals(MemberRole.TEACHER)) {
			member = Member.createTeacher(
				request.email(),
				request.name(),
				request.password(),
				request.secondaryPassword());
		} else {
			throw new BusinessException(INVALID_ROLE_SELECTION);
		}

		// 저장
		memberRepository.save(member);

		// 반환
		return new RegisterResponse(member.getEmail(), member.getMemberRole());
	}

	@Transactional(readOnly = true)
	public void validateRegisterEmail(String inputEmail) {
		isValidEmail(inputEmail);
		if (memberRepository.findByEmail(inputEmail).isPresent()) {
			throw new BusinessException(EMAIL_ALREADY_REGISTERED);
		}
		// 이메일 중복체크 통과 시 redis에 5분간 저장
		redisTemplate.opsForValue().set("email:" + inputEmail, inputEmail, 5, TimeUnit.MINUTES);
	}
}
