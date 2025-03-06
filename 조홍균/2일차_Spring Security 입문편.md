# [2025-03-05] Spring Security 입문편

## 🎯 학습 목표

- Spring Security 개념과 특징 이해
- Spring Security를 왜 쓰는가?
- Spring Security Filter Chain

## 📌 Spring Security 개념과 특징 이해

- 인증 + 권한 관리 + 데이터 보호 기능 등 사용자 관리 기능을 구현하는데 도움을 주는 Spring의 Framework 중 하나
- 회원가입, 로그인, 로그아웃, 세션 관리, 권한 관리 등 다양한 기능이 구현되어 있는 Interface 및 Class 제공
- 개발자들이 보안 관련 기능을 효율적이고 신속하게 구현할 수 있도록 도와준다
- 폼 로그인, HTTP Basic, OAuth2, JWT 등 다양한 인증 방식도 지원한다

## 📌 Spring Security를 왜 쓰는가?

- Spring의 생태계에서 보안에 필요한 기능들이 이미 구현되어 있고, 제공해주기 때문(개발 리소스 절약!)
- Spring Framework에 활용하기 적합한 구조로 설계되어 있어 최적화된 보안 기능들을 사용할 수 있기 때문

## 📌 Spring Security Filter Chain

- 사용자의 Request가 Servlet에 전달되기 전에 거치는 일련의 Filter들의 모음인 FilterChain을 통해 보안 로직을 적용. 인증, 권한 부여, CSRF(Cross-Site Request Forgery) 보호 등의 기능을 수행한다
- 수행 과정
  1. http request(user request)가 서버로 들어옴
  2. Spring Security의 Filter Chain을 거쳐 인증과 인가 단계를 거침
  3. 인증/인가된 유저의 경우 기타 Filter(있는 경우)로 전달됨.
  4. 모든 Filter를 통과한 유저는 Servlet을 통해 Controller로 request를 전달
  5. 비즈니스 로직 수행

### 🔹 코드 예제

> 하기 코드는 Claude Sonnet 3.7 Extended 모델이 생성한 예시 코드입니다. 실제 프로젝트를 개발할 때에는 레거시 코드와 함께 비교하며 가장 적절한 방식으로 코드를 구성할 것 같습니다.

```
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }
}
```

### 결론

> [!note] Spring Security는 Spring Framework 내에서 강력하고 유연한 인증/권한 관련 기능들을 구현하는 데 유용한 프레임워크입니다. 필요시, 다형성을 통해서 다양한 방식으로 확장도 가능할 것 같습니다.
> 기존의 코드를 변경하지 않고도, FilterChain을 통해서 추가적인 보안로직을 세팅할 수 있다는 점이 가장 매력적인 것 같습니다.

## 🔗 참고자료 (필요 시)

- [Spring Security Filter Chain](https://memodayoungee.tistory.com/134)
- [Cladue Sonnet 3.7 Extended의 Spring Security 입문편](https://claude.site/artifacts/5d03bb76-5499-4156-8dc0-068a97e0ee1b)
