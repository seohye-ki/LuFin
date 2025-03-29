package com.lufin.server.member.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;
import static com.lufin.server.member.util.MemberValidator.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.common.utils.TokenUtils;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.dto.LoginResponse;
import com.lufin.server.member.repository.MemberRepository;
import com.lufin.server.member.service.LoginService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

	private static final String LOGIN_FAIL_PREFIX = "login_fail:";
	private static final int MAX_LOGIN_FAIL_COUNT = 5;
	private static final long LOGIN_BLOCK_DURATION_MINUTES = 5;
	private final MemberRepository memberRepository;
	private final MemberClassroomRepository memberClassroomRepository;
	private final TokenUtils tokenUtils;
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional
	@Override
	public LoginResponse login(String inputEmail, String inputPassword) {

		// 입력값 검증
		isValidEmail(inputEmail);
		isValidPassword(inputPassword);

		String failKey = LOGIN_FAIL_PREFIX + inputEmail;

		log.info("[로그인 요청] 이메일: {}", maskEmail(inputEmail));

		// 로그인 차단 여부 확인
		loginFailCheck(failKey);

		Member member = memberRepository.findByEmail(inputEmail)
			.orElseThrow(() -> {
				log.warn("[로그인 실패] 존재하지 않는 이메일: {}", maskEmail(inputEmail));
				increaseLoginFailCount(failKey);
				return new BusinessException(INVALID_CREDENTIALS);
			});

		if (!member.getAuth().isPasswordMatch(inputPassword)) {
			log.warn("[로그인 실패] 비밀번호 불일치 - 이메일: {}", maskEmail(inputEmail));
			increaseLoginFailCount(failKey);
			throw new BusinessException(INVALID_CREDENTIALS);
		}

		// 로그인 성공 시 실패 카운트 초기화
		redisTemplate.delete(failKey);
		log.info("[로그인 성공] 사용자 ID: {}", member.getId());

		// Token 발급
		Result getTokens = createTokens(member);

		// 로그인 성공
		member.updateLastLogin();

		log.info("[로그인 완료] 사용자 ID: {}, 역할: {}", member.getId(), member.getMemberRole().name());

		return new LoginResponse(getTokens.accessToken(), getTokens.refreshToken(), member.getMemberRole().name());
	}

	// 사용자 정보로 액세스 토큰과 리프레시 토큰을 생성
	private Result createTokens(Member member) {
		String accessToken;
		// Optional<MemberClassroom> 현재 로그인한 멤버가 클래스에 등록되어 있다면 값이 있고, 아니면 비어있음
		Optional<MemberClassroom> optionalClassroom = memberClassroomRepository.findByMember_IdAndIsCurrentTrue(
			member.getId());

		if (optionalClassroom.isPresent()) {
			int classId = optionalClassroom.get().getClassroom().getId();
			log.info("[토큰 발급] 클래스 ID 포함 - 사용자 ID: {}, 클래스 ID: {}", member.getId(), classId);
			accessToken = tokenUtils.createAccessToken(member.getId(), member.getMemberRole(), classId);
		} else {
			log.info("[토큰 발급] 클래스 ID 없음 - 사용자 ID: {}", member.getId());
			accessToken = tokenUtils.createAccessToken(member.getId(), member.getMemberRole());
		}

		String refreshToken = tokenUtils.createRefreshToken(
			member.getId(),
			member.getMemberRole(),
			// 클래스 ID가 있을 경우에는 해당 ID, 없을 경우에는 기본값 0 사용
			optionalClassroom.map(c -> c.getClassroom().getId()).orElse(0));
		return new Result(accessToken, refreshToken);
	}

	private void loginFailCheck(String failKey) {
		String failCountStr = redisTemplate.opsForValue().get(failKey);
		int failCount = failCountStr != null ? Integer.parseInt(failCountStr) : 0;

		if (failCount >= MAX_LOGIN_FAIL_COUNT) {
			Long ttl = redisTemplate.getExpire(failKey, TimeUnit.SECONDS);
			if (ttl != null && ttl > 0) {
				log.warn("[로그인 차단] 키: {}, 남은 차단 시간(초): {}", failKey, ttl);
				throw new BusinessException(ACCOUNT_TEMPORARILY_LOCKED);
			}
		}
	}

	private void increaseLoginFailCount(String failKey) {
		Long count = redisTemplate.opsForValue().increment(failKey);

		if (count == null) {
			log.error("[로그인 실패] Redis 오류로 실패 횟수를 가져오지 못했습니다.");
			return;
		}

		// 실패 횟수가 MAX를 처음으로 초과한 경우에만 TTL 설정
		if (count == MAX_LOGIN_FAIL_COUNT + 1) {
			redisTemplate.expire(failKey, LOGIN_BLOCK_DURATION_MINUTES, TimeUnit.MINUTES);
			log.warn("[로그인 차단 시작] 키: {}, 실패 횟수: {}, 차단 시간(분): {}", failKey, count, LOGIN_BLOCK_DURATION_MINUTES);
		} else {
			// TTL 연장하지 않음 (누적만)
			log.warn("[로그인 실패 횟수 증가] 키: {}, 현재 실패 횟수: {}", failKey, count);
		}
	}

	// 예: example@gmail.com -> e***e@gmail.com
	private String maskEmail(String email) {
		int atIndex = email.indexOf('@');
		if (atIndex <= 1) {
			return email; // 이메일 형식이 아닌 경우 그대로 반환
		}

		String localPart = email.substring(0, atIndex);
		String domainPart = email.substring(atIndex + 1);

		String maskedLocal = localPart.charAt(0)
			+ "***"
			+ (localPart.length() > 1 ? localPart.charAt(localPart.length() - 1) : "");

		// 도메인은 가리지 않음
		return maskedLocal + "@" + domainPart;
	}

	private record Result(String accessToken, String refreshToken) {
	}
}
