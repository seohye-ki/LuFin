package com.lufin.server.auth.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;
import static com.lufin.server.member.util.MaskingUtil.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.lufin.server.auth.dto.LoginWithAssetResponse;
import com.lufin.server.auth.factory.LoginResponseFactory;
import com.lufin.server.auth.service.LoginFacadeService;
import com.lufin.server.auth.service.LoginService;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.common.utils.TokenUtils;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.repository.MemberRepository;

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
	private final LoginResponseFactory loginResponseFactory;
	private final LoginFacadeService loginFacadeService;

	@Transactional
	@Override
	public LoginWithAssetResponse login(String inputEmail, String inputPassword) {
		String failKey = LOGIN_FAIL_PREFIX + inputEmail;
		log.info("[로그인 요청] 이메일: {}", maskEmail(inputEmail));

		loginFailCheck(failKey);

		if (!StringUtils.hasText(inputEmail) || !StringUtils.hasText(inputPassword)) {
			log.warn("🔐[로그인 실패 - 입력값 오류] 이메일: {}", maskEmail(inputEmail));
			increaseLoginFailCount(failKey);
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		Member member = memberRepository.findByEmail(inputEmail)
			.orElseThrow(() -> {
				log.warn("🔐[로그인 실패] 존재하지 않는 이메일: {}", maskEmail(inputEmail));
				increaseLoginFailCount(failKey);
				return new BusinessException(INVALID_CREDENTIALS);
			});

		if (!member.getAuth().isPasswordMatch(inputPassword)) {
			log.warn("🔐[로그인 실패] 비밀번호 불일치 - 이메일: {}", maskEmail(inputEmail));
			increaseLoginFailCount(failKey);
			throw new BusinessException(INVALID_CREDENTIALS);
		}

		redisTemplate.delete(failKey);
		log.info("[로그인 성공] 사용자 ID: {}", member.getId());

		Optional<MemberClassroom> optionalClassroom = memberClassroomRepository.findByMember_IdAndIsCurrentTrue(
			member.getId());
		int classId = optionalClassroom.map(c -> c.getClassroom().getId()).orElse(0);

		Result getTokens = createTokens(member, optionalClassroom);
		member.updateLastLogin();

		log.info("[로그인 완료] 사용자 ID: {}, 이름: {}, Role: {}, 소속 반: {}", member.getId(), maskName(member.getName()),
			member.getMemberRole().name(), classId);

		int totalAsset = loginFacadeService.getTotalAsset(member.getId(), classId);

		return loginResponseFactory.createLoginFlatResponse(
			member,
			classId,
			getTokens.accessToken(),
			getTokens.refreshToken(),
			totalAsset
		);
	}

	// 사용자 정보로 액세스 토큰과 리프레시 토큰을 생성
	private Result createTokens(Member member, Optional<MemberClassroom> optionalClassroom) {
		String accessToken;

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
				log.warn("🔐[로그인 차단] 키: {}, 남은 차단 시간(초): {}", failKey, ttl);
				throw new BusinessException(ACCOUNT_TEMPORARILY_LOCKED);
			}
		}
	}

	private void increaseLoginFailCount(String failKey) {
		Long count = redisTemplate.opsForValue().increment(failKey);

		if (count == null) {
			log.error("🔐[로그인 실패] Redis 오류로 실패 횟수를 가져오지 못했습니다.");
			return;
		}

		// 실패 횟수가 MAX를 처음으로 초과한 경우에만 TTL 설정
		if (count == MAX_LOGIN_FAIL_COUNT + 1) {
			redisTemplate.expire(failKey, LOGIN_BLOCK_DURATION_MINUTES, TimeUnit.MINUTES);
			log.warn("🔐[로그인 차단 시작] 키: {}, 실패 횟수: {}, 차단 시간(분): {}", failKey, count, LOGIN_BLOCK_DURATION_MINUTES);
		} else {
			// TTL 연장하지 않음 (누적만)
			log.warn("🔐[로그인 실패 횟수 증가] 키: {}, 현재 실패 횟수: {}", failKey, count);
		}
	}

	private record Result(String accessToken, String refreshToken) {
	}
}
