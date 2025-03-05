# [2025-03-05] 2일차_엔티티 매핑과 기본 CRUD

## 🎯 학습 목표
- 데이터베이스 테이블을 객체(Entity)로 매핑하고, 기본 CRUD 기능 구현

## 📌 엔티티(Entity)란?
- 데이터베이스 테이블과 매핑되는 객체를 의미
- SQL 테이블을 직접 다루지 않고 엔티티 클래스를 통해 데이터 조작이 가능

### 🔹 기존 JDBC 방식
```sql
INSERT INTO member (name) VALUES ('Alice');
SELECT * FROM member WHERE id = 1;
UPDATE member SET name = 'Bob' WHERE id = 1;
DELETE FROM member WHERE id = 1;
```

### 🔹 JPA 방식 (객체 사용)
```java
Member member = new Member("Alice");

// INSERT
memberRepository.save(member);

// SELECT
Member findMember = memberRepository.findById(1L).orElse(null); 

// UPDATE
findMember.updateName("Bob");
memberRepository.save(findMember);

//DELETE
memberRepository.delete(findMember);
```
### 🔹 JPA 방식의 장점
- SQL을 직접 작성하지 않아도 됨
- 객체 지향적인 코드 작성 가능
- 데이터베이스 변경 시 SQL을 수정할 필요가 없음

## 📌 엔티티 매핑(Entity Mapping)
- 엔티티 클래스에 애너테이션을 사용하여 테이블과 매핑

### 🔹 `@Entity` 기본 설정
- 엔티티 생성 시 클래스에 `@Entity` 애너테이션을 추가

```java
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity  // JPA 엔티티 선언
@Table(name = "member")  // 테이블 이름 지정 (생략 가능)
public class Member {

    @Id  // 기본 키(PK) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT 적용
    private Long id;

    @Column(nullable = false, length = 50)  // NOT NULL 적용, 길이 제한
    private String name;

    public Member(String name) {
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }
}

```


## 📌 JPA 기본 CRUD 구현

### 🔹 JpaRepository
```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> { // Member 엔티티의 기본 CRUD 제공
}
```
### 🔹 회원 저장 (Create)
- `memberRepository.save()` 메서드를 사용하면 INSERT SQL이 자동 실행 됨

```java
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;

    @Transactional
    public Member saveMember(String name) {
        return memberRepository.save(new Member(name));
    }
}
```

### 🔹 회원 조회 (Read)
- `findById()` 메서드를 사용하면 SELECT SQL이 자동 실행 됨

``` java
public Member getMember(Long id) {
    return memberRepository.findById(id).orElse(null);
}
```

### 🔹 회원 수정 (Update)
- JPA는 변경 감지(dirty checking) 기능을 사용하여, `save()` 없이도 자동 업데이트를 수행 함

```java
@Transactional
public void updateMember(Long id, String newName) {
    Member member = memberRepository.findById(id).orElseThrow();
    member.updateName(newName);
}
```

### 🔹 회원 삭제 (Delete)
- `deleteById()` 메서드를 사용하면 DELETE SQL이 자동 실행 됨

```java
@Transactional
public void deleteMember(Long id) {
    memberRepository.deleteById(id);
}
```

## 🔗 참고자료

- [Spring Data JPA 공식 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Boot 공식 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Hibernate 공식 문서](https://hibernate.org/orm/documentation/6.6/)
- [Spring Boot + JPA 가이드](https://spring.io/guides/gs/accessing-data-jpa/)
