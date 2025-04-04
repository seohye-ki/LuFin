package com.lufin.server.auth.service.impl;

import org.springframework.stereotype.Service;

import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.auth.dto.TokenResponse;
import com.lufin.server.auth.service.RefreshTokenService;
import com.lufin.server.common.constants.TokenClaimName;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.common.utils.TokenUtils;
import com.lufin.server.member.domain.MemberRole;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final TokenUtils tokenUtils;
	private final AccountRepository accountRepository;

	@Override
	public TokenResponse reissueAccessToken(String refreshToken) throws BusinessException {
		log.info("[토큰 재발급 요청]");

		// 새로운 Access Token 발급
		String newAccessToken = tokenUtils.refreshAccessToken(refreshToken);

		// Refresh Token 클레임 추출
		Claims claims = tokenUtils.extractClaims(refreshToken);

		// 클레임에서 필요한 정보 추출
		int userId = Integer.parseInt(claims.getSubject());
		MemberRole role = MemberRole.valueOf((String)claims.get(TokenClaimName.ROLE));

		// classId가 있으면 추출, 없으면 기본값 0 설정
		int classId = 0;
		if (claims.get(TokenClaimName.CLASS_ID) != null) {
			classId = Integer.parseInt((String)claims.get(TokenClaimName.CLASS_ID));
		}

		log.info("[토큰 재발급 요청] memberId = {}, role = {}, classId = {}", userId, role, classId);

		// 계좌번호 조회 (없을 경우 빈 문자열)
		String accountNumber = "";
		try {
			accountNumber = accountRepository.findByMemberIdAndClosedAtIsNull(userId).get().getAccountNumber();
		} catch (Exception e) {
			// 계좌번호가 없어도 토큰 재발급은 가능하도록 예외 무시
		}

		log.info("[토큰 재발급 완료] memberId = {}, role = {}, classId = {}", userId, role, classId);

		// TokenResponse 객체 생성 및 반환
		return new TokenResponse(
			newAccessToken,
			refreshToken,
			role.name(),
			classId,
			accountNumber
		);
	}
}
