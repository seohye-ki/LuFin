package com.lufin.server.common.utils;

import static com.lufin.server.common.constants.ErrorCode.*;
import static com.lufin.server.common.constants.TokenType.*;

import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.lufin.server.common.constants.TokenClaimName;
import com.lufin.server.common.constants.TokenType;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.MemberRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenUtils {

	private final SecretKey SECRET_KEY;
	private final long ACCESS_TOKEN_EXPIRATION_TIME;
	private final long REFRESH_TOKEN_EXPIRATION_TIME;
	private final RedisTemplate<String, String> redisTemplate;

	public TokenUtils(
		@Value("${JWT.SECRET_KEY}") String secretKeyBase64,
		@Value("${JWT.EXPIRATION_TIME.ACCESS}") long accessTokenExpiration,
		@Value("${JWT.EXPIRATION_TIME.REFRESH}") long refreshTokenExpiration,
		RedisTemplate<String, String> redisTemplate
	) {
		this.SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyBase64));
		this.ACCESS_TOKEN_EXPIRATION_TIME = accessTokenExpiration;
		this.REFRESH_TOKEN_EXPIRATION_TIME = refreshTokenExpiration;
		this.redisTemplate = redisTemplate;
	}

	// AccessToken (userId, role)
	public String createAccessToken(int userId, MemberRole role) {
		Claims claims = Jwts.claims()
			.subject(String.valueOf(userId))
			.add(TokenClaimName.TYPE, TokenType.ACCESS)
			.add(TokenClaimName.ROLE, role.name())
			.build();

		return generateToken(claims, ACCESS_TOKEN_EXPIRATION_TIME);
	}

	// AccessToken (userId, role, classId)
	public String createAccessToken(int userId, MemberRole role, int classId) {
		Claims claims = Jwts.claims()
			.subject(String.valueOf(userId))
			.add(TokenClaimName.TYPE, TokenType.ACCESS)
			.add(TokenClaimName.ROLE, role.name())
			.add(TokenClaimName.CLASS_ID, String.valueOf(classId))
			.build();

		return generateToken(claims, ACCESS_TOKEN_EXPIRATION_TIME);
	}

	// Refresh Token 생성 및 Redis에 저장
	public String createRefreshToken(int userId, MemberRole role, int classId) {
		Claims claims = Jwts.claims()
			.subject(String.valueOf(userId))
			.add(TokenClaimName.TYPE, TokenType.REFRESH)
			.add(TokenClaimName.ROLE, role.name())
			.add(TokenClaimName.CLASS_ID, String.valueOf(classId))
			.build();

		String refreshToken = generateToken(claims, REFRESH_TOKEN_EXPIRATION_TIME);

		// Redis에 저장
		redisTemplate.opsForValue()
			.set("refresh_token:" + userId, refreshToken, REFRESH_TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
		return refreshToken;

	}

	// RefreshToken으로 AccessToken 재발급 (RefreshToken → Claims → userId 추출)
	public String refreshAccessToken(String refreshToken) {
		Claims claims = extractClaims(refreshToken);

		if (!REFRESH.equals(claims.get(TokenClaimName.TYPE))) {
			throw new BusinessException(INVALID_TOKEN);
		}

		int userId = Integer.parseInt(claims.getSubject());
		String key = "refresh_token:" + userId;
		String storedToken = redisTemplate.opsForValue().get(key);

		if (storedToken == null || !storedToken.equals(refreshToken)) {
			throw new BusinessException(INVALID_TOKEN);
		}

		MemberRole role = MemberRole.valueOf((String)claims.get(TokenClaimName.ROLE));
		String classIdStr = (String)claims.get(TokenClaimName.CLASS_ID);

		if (classIdStr != null) {
			return createAccessToken(userId, role, Integer.parseInt(classIdStr));
		}
		return createAccessToken(userId, role);
	}

	// 로그아웃 시 RefreshToken 삭제
	public void deleteRefreshToken(String refreshToken) {
		Claims claims = extractClaims(refreshToken);
		if (!REFRESH.equals(claims.get(TokenClaimName.TYPE))) {
			throw new BusinessException(INVALID_TOKEN_TYPE);
		}
		int userId = Integer.parseInt(claims.getSubject());
		redisTemplate.delete("refresh_token:" + userId);
	}

	// 공통: JWT 생성
	private String generateToken(Claims claims, long expireTimeMs) {
		Date now = new Date();
		Date expire = new Date(now.getTime() + expireTimeMs);

		return TokenClaimName.BEARER_PREFIX + Jwts.builder()
			.claims(claims)
			.issuedAt(now)
			.expiration(expire)
			.signWith(SECRET_KEY, Jwts.SIG.HS256)
			.compact();
	}

	// 공통: Token에서 Claims 추출
	public Claims extractClaims(String token) {
		if (token.startsWith(TokenClaimName.BEARER_PREFIX)) {
			token = token.substring(TokenClaimName.BEARER_PREFIX.length());
		}
		return Jwts.parser()
			.verifyWith(SECRET_KEY)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
