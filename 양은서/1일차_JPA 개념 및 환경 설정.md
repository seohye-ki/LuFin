# [2025-03-04] 1일차_JPA 개념 및 환경 설정

## 🎯 학습 목표
- JPA 개념 및 환경 설정

## 📌 JPA란?
- 자바에서 데이터베이스를 쉽게 다룰 수 있도록 해주는 ORM 기술
- SQL을 직접 작성하지 않아도 객체(Entity)와 테이블을 자동으로 매핑

### 🔹 기존 방식과 비교
- JDBC
    - MySQL 드라이버를 직접 사용
    - SQL을 직접 작성하고 `ResultSet`을 수동으로 매핑
- MyBatis
    - SQL을 XML 또는 애너테이션으로 관리
    - SQL을 보다 깔끔하게 관리하지만, 여전히 SQL 중심
- JPA
    - 객체 중심 개발
    - SQL을 직접 작성할 필요 없이, 엔티티 객체만 다루면 됨

## 📌 JPA 환경 설정
### 🔹 application.yml
```
spring:
  datasource:
    url: jdbc:h2:mem:testdb  # 인메모리 DB 사용
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: update  # 애플리케이션 실행 시 테이블 자동 생성/수정
    properties:
      hibernate:
        format_sql: true  # SQL 예쁘게 출력
    show-sql: true  # 콘솔에 SQL 출력
  h2:
    console:
      enabled: true  # H2 웹 콘솔 활성화
```
→ H2 DB가 자동으로 실행/ `http://localhost:8080/h2-console`에서 확인 가능

## 📌 JPA 기본 엔티티(Entity) 생성
- Entity 클래스 생성
- Entity: 데이터베이스의 테이블과 연결되는 객체

### 🔹 예제) Member
```
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity  // JPA가 관리하는 엔티티 객체 (DB 테이블과 매핑됨)
public class Member {

    @Id  // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가 (AUTO_INCREMENT)
    private Long id;

    @Column(nullable = false)  // NOT NULL 적용
    private String name;

    public Member(String name) {
        this.name = name;
    }
}
```

## 📌 JPA Repository 생성
- `JpaRepository` 인터페이스를 상속받아 DB와 연동

### 🔹 예제) MemberRepository
```
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> { // Member 엔티티의 기본 CRUD 제공
}
```

## 🔗 참고자료

- [Spring Data JPA 공식 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Boot 공식 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Hibernate 공식 문서](https://hibernate.org/orm/documentation/6.6/)
- [Spring Boot + JPA 가이드](https://spring.io/guides/gs/accessing-data-jpa/)
