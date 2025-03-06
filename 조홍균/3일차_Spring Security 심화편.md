# [2025-03-06] Spring Security 심화편

## 🎯 학습 목표

- UserDetails에 대한 이해
- SecurityContext에 대한 이해

## 📌UserDetails

### 개념

- Spring Security에서 인증된 사용자의 정보를 담는 인터페이스입니다.
- 계정 정보(아이디, 비밀번호), 권한, 상태(잠금, 만료 등)이 사용자 정보에 포함됩니다.

### 장점

- 사용자 정보가 어떻게 저장되는지와 무관하게 Spring Security가 표준화된 방식으로 접근할 수 있게 해줍니다.
- RDBMS, NoSQL 등 다양한 저장소와 통합할 수 있는 일관된 인터페이스를 제공합니다.
- 사용자 관리 로직과 보안 프레임워크를 분리해 유연성을 제공합니다.

### 🔹 코드 예제

구현 코드

```
public interface UserDetails extends Serializable {
    Collection<? extends GrantedAuthority> getAuthorities();
    String getPassword();
    String getUsername();
    boolean isAccountNonExpired();
    boolean isAccountNonLocked();
    boolean isCredentialsNonExpired();
    boolean isEnabled();
}
```

> 위 코드에서 `getUsername()`에 주목할 필요가 있다고 합니다. username은 계정의 고유한 값을 가지는 속성인데, email과 같은 값이 중복이 될 수 있는 경우가 있을 수 있기에 주의해야합니다.

### CustomUserDetails

개발자가 필요에 따라 커스터마이징한 객체입니다. UserDetails 인터페이스를 구현하는 구현체로, 다형성을 통해 기능을 확장할 수 있습니다.

### UserDetailService

유저의 정보를 가져오는 인터페이스입니다.
`loadUserByUsername()`이라는 단일 메서드를 가지고 있습니다.
유저의 정보를 불러와 UserDetails로 리턴하는 기능을 합니다.

## 📌SecurityContext

### 개념

- 현재 인증된 사용자의 보안 정보를 저장하는 인터페이스입니다.
- `Authentication`객체를 통해 현재 인증된 사용자를 나타냅니다.
- SecurityContextHolder에 의해 관리되고, 애플리케이션 위치와 관계 없이 현재 인증된 사용자의 정보에 접근할 수 있게 합니다.

### Authentication

사용자의 인증 정보를 저장하는 토큰과도 같은 객체입니다.

1. 인증시 id와 pw를 담고 인증 검증을 위해 전달되어 사용되기도 하고
2. 인증 후 최종 인증 결과를 담고 SecurityContext에 저장되어 전역 참조가 가능하게 만들어 줍니다.
   `Authentication authentication = SecurityContexHolder.getContext().getAuthentication()`

> 공통 프로젝트 때, Back-end에서 DB Connection 이슈 때문에 DB가 다운되는 이슈가 있었습니다.
> 이는 SSE 연결을 위한 stream을 열 때 유저 정보를 DB에서 조회하는 것이 원인이었습니다. DB를 조회하는 Connection Pool을 여는데, 한 번에 많은 유저가 로그인을 할 경우에 지정된 Pool을 넘어서 Sleep하게 되기 때문입니다.
> 이를 SecurityContext가 가지고 있는 Authentication 객체에서 참조하는 것으로 변경해 DB 조회 로직을 없애 해결했었기 때문에, 유독 친근하게 느껴지는 개념입니다.

### 🔹 코드 예제

> 하기 코드는 공통 프로젝트 때 Back-end 리더가 구성한 유저 조회 메서드입니다. SecurityContextHolder에 있는 Authentication 객체를 통해 인증된 사용자의 정보를 조회하는 로직을 볼 수 있습니다.

```
@Transactional(readOnly = true)
	public User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
			log.warn("SecurityContext에 Authentication이 없습니다. 사용자가 로그인하지 않았을 가능성이 큽니다.");
			return null;
		}

		Object principal = authentication.getPrincipal();

		if (!(principal instanceof CustomOAuth2User)) {
			log.error("Principal이 CustomOAuth2User 타입이 아닙니다. 현재 타입: {}", principal.getClass().getName());
			return null;
		}

		CustomOAuth2User customUser = (CustomOAuth2User)principal;
		String personalId = customUser.getPersonalId();
		log.info("Personal ID: {}", personalId);

		User user = userRepository.findByPersonalId(personalId)
			.orElseGet(null);

		log.info("email: {}", user.getEmail());

		return user;
	}
```

### 결론

1. 사용자가 로그인할 경우 가장 먼저 `UserDetailService`를 통해 `UserDetails`를 불러옵니다. (`loadUserByUsername()`)
2. 인증 성공 시, `UserDetails` 정보를 기반으로 `Authentication` 객체를 생성합니다.
3. 이는 `SecurityContext` 안에 저장되며, `SecurityContextHolder`를 통해 접근하고 관리할 수 있습니다.

## 🔗 참고자료

- [UserDetails 관련 블로그](https://programmer93.tistory.com/68)
- [Authentication, SecurityContext 관련 블로그](https://velog.io/@gmtmoney2357/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-Authentication-SecurityContext)
