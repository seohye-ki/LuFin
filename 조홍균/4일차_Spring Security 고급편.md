# [2025-03-07] Spring Security 고급편

## 🎯 학습 목표

- Spring Security의 권한 부여에 대한 이해
- JWT(JSON Web Token)에 대한 이해

## 📌 Spring Security의 권한에 대한 이해

### 개념

- 권한 부여(Authorization)는 인증(Authentication)된 사용자가 어떤 자원에 접근할 수 있는 지 결정하는 프로세스 입니다.
  - 인증은 그 사용자가 적법한 사용자인지 판단하고, 권한은 인증된 사용자가 어떤 직책을 가지는지를 판단하는 느낌입니다.
- Spring Security는 `권한(Authority)`와 `역할(Role)`로 권한 부여를 구현합니다.
  - **권한**: 특정 작업을 수행할 수 있는 권리(CRUD 등)
  - **역할**: 관련 권한들의 그룹(Admin, User, 등)
    - Spring Security에서 역할은 `ROLE_` 접두사를 가진 권한으로 처리됩니다.

### 🔹 코드 예제

> 하기 코드는 URL 기반 권한 설정을 위한 예시 코드입니다. Claude 3.7 Sonnet Extended에 의해 생성되었습니다. 실제 구현할 때에는 레거시 코드와 함께 비교하며 가장 필요한 부분을 취할 필요가 있어보입니다.

```
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/users/**").hasRole("USER")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/superadmin/**").hasAuthority("SUPER_ADMIN_ACCESS")
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
```

## 📌 JWT(JSON Web Token)에 대한 이해

### 개념

- JWT는 JSON 객체를 기반으로 정보를 안전하게 전송하기 위한 개방형 표준 방식입니다.
- 디지털 서명이 되어있어 신뢰할 수 있고, 공개/개인 key 쌍으로 서명할 수 있습니다.
- Stateless한 서버 연결 형태를 유지하면서도 인증 정보를 유지하기 위해 사용했던 기억이 납니다.

### 구조

JWT는 세 부분으로 구성되며, 각 부분은 점(.)으로 구분됩니다:

- 헤더(Header): 토큰 유형과 서명 알고리즘
- 페이로드(Payload): 클레임(Claim) 정보
- 서명(Signature): 토큰이 유효한지 확인하는 데 사용

예시)
`eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0ODUxNDA5ODQsImlhdCI6MTQ4NTEzNzM4NCwiaXNzIjoiYWNtZS5jb20iLCJzdWIiOiIyOWFjMGMxOC0wYjRhLTQyY2YtODJmYy0wM2Q1NzAzMThhMWQiLCJhcHBsaWNhdGlvbklkIjoiNzkxMDM3MzQtOTdhYi00ZDFhLWFmMzctZTAwNmQwNWQyOTUyIiwicm9sZXMiOltdfQ.Mp0Pcwsz5VECK11Kf2ZZNF_SMKu5CgBeLN9ZOP04kZo
`

### 🔹 코드 예제

> 하기 코드는 1학기 최종 관통 프로젝트에서 제가 구현했던 JWT Util 클래스입니다. 이번 프로젝트에서 구현할 때에는 참고하면서 더 개선할 점을 고민해볼 계획입니다.

```
package com.ssafit.util;

import java.security.Key;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ssafit.model.dto.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
/*
 * JWT 발행, 토큰 생성, 토큰 검증 등의 역할을 하는 클래스
 */
public class JwtUtil {
	//-----------------------------------------------------------//
	// 멤버 필드
	//-----------------------------------------------------------//
    private final Key secretKey; // signWith에서 사용하기 위한 key 객체
    private static final Long expiration = 1000L * 60 * 60 * 24; // 밀리세컨드 기준 일단 하루

    /**
     * 생성자로 application.properties 설정 값 주입
     * @param (String) secret
     * @param (Long) expiration
     */
	public JwtUtil(@Value("${jwt.secret}") String secret) {
		byte[] keyBytes = Decoders.BASE64.decode(secret); // Key 객체로 만들기 위해 byte로 디코딩
		this.secretKey = Keys.hmacShaKeyFor(keyBytes); // Keys를 통해 Key 객체로 변환
	}

	//-----------------------------------------------------------//
	// JWT Building
	//-----------------------------------------------------------//

	/** JWT 생성
	 * @param user
	 * @param expiration
	 * @return
	 */
	private String createToken(User user, long expiration) {
		Claims claims = Jwts.claims();
		// jwt에 key: value 형태로 저장
		// TODO 1. token에 노출하지 않을 정보 삭제하기
		claims.put("userId", user.getId());
		claims.put("userName", user.getUserName());
		claims.put("score", user.getScore());
		claims.put("tier", user.getTier());
		claims.put("totalCardCount", user.getTotalCardCount());

		// 한국 시간만이면 ZoneId.of("Asia/Seoul")을 now()의 인자로
		ZonedDateTime now = ZonedDateTime.now(); // 지금 시간
		ZonedDateTime tokenValidity = now.plusSeconds(expiration); // 지금에 만료 시간 더한 값

		// jwt 반환
		return Jwts.builder()
				.setClaims(claims) // claim 담기
				.setIssuedAt(Date.from(now.toInstant())) // jwt가 발급된 때
				.setExpiration(Date.from(tokenValidity.toInstant())) // 만료일
				.signWith(secretKey, SignatureAlgorithm.HS256) // sign
				.compact();
	}

	//-----------------------------------------------------------//
	// Generate Tokens
	//-----------------------------------------------------------//
	/** Access Token 생성
	 * @param (User) user
	 * @return (String) Access Token
	 */
    public String createAccessToken(User user) {
        return createToken(user, expiration);
    }

	//-----------------------------------------------------------//
	// Verifying Token
	//-----------------------------------------------------------//
    /** token 유효성 검사
     * @param token
     * @return Boolean
     * true/false
     */
    public boolean verifyToken(String token) {
        try {
        	// parsing
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        }

        // 예외 처리
        // 1. 유효하지 않음
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
        	System.out.println("Invalid JWT Token");
        	e.printStackTrace();
        }
        // 2. 만료됨
        catch (ExpiredJwtException e) {
        	System.out.println("Expired JWT Token");
        	e.printStackTrace();
        }
        // 3. 지원하지 않음
        catch (UnsupportedJwtException e) {
        	System.out.println("Unsupported JWT Token");
        	e.printStackTrace();
        }
        // 4. 빈 값임
        catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty.");
            e.printStackTrace();
        }
        return false;
        // 출처: https://sjh9708.tistory.com/170 [데굴데굴 개발자의 기록:티스토리]
    }

	//-----------------------------------------------------------//
	// Extract Information
	//-----------------------------------------------------------//
    /** Access Token에서 JWT Claims 추출
     * @param (String) accessToken
     * @return (Claims) JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
            		.setSigningKey(secretKey)
            		.build()
            		.parseClaimsJws(accessToken)
            		.getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /** Token에서 User Id 추출
     * @param (String) token
     * @return (int) userId
     */
    public int getUserId(String token) {
    	return parseClaims(token).get("userId", Integer.class);
    }

    /** token에서 userName 추출
     * @param token
     * @return (String) userName
     */
    public String getUserName(String token) {
    	return parseClaims(token).get("userName", String.class);
    }

}
```

## 🔗 참고자료

- [권한 부여](https://cordcat.tistory.com/97)
- [쿠키와 세션](https://interconnection.tistory.com/74)
