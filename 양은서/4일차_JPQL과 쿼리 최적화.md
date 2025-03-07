# [2025-03-07] 4일차\_JPQL과 쿼리 최적화

## 🎯 학습 목표

- 데이터 조회 성능 최적화하는 방법을 학습
- JPQL을 사용한 쿼리 작성 방법 학습

## 📌 JPQL (Java Persistence Query Language)

- JPA에서 제공하는 객체지향 쿼리 언어
- SQL과 유사하지만, 테이블이 아닌 엔티티 객체를 대상으로 쿼리를 작성
- JPA가 SQL로 변환하여 실행

### 🔹 기존 SQL 방식

```sql
SELECT * FROM member WHERE name = 'Alice';
```

### 🔹 JPA 방식 (JPQL)

```sql
SELECT m FROM Member m WHERE m.name = 'Alice';
```

### 🔹 차이점

- **SQL**: 테이블(member)을 대상으로 쿼리를 작성
- **JPQL**: 엔티티 객체(Member)를 대상으로 쿼리를 작성

## 📌 기본 JPQL 예제

### 🔹 특정 이름을 가진 회원 조회

```java
TypedQuery<Member> query = em.createQuery(
    "SELECT m FROM Member m WHERE m.name = :name", Member.class);
query.setParameter("name", "Alice");
List<Member> members = query.getResultList();
```

- `SELECT m FROM Member m WHERE m.name = :name`
  - `Member` 엔티티를 대상으로 쿼리를 작성
  - `:name`은 파라미터 바인딩을 위한 이름 (동적 쿼리)

### 🔹 전체 회원 조회

```java
List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
                        .getResultList();
```

### 🔹 특정 필드만 조회 (DTO 매핑)

```java
List<String> names = em.createQuery("SELECT m.name FROM Member m", String.class)
                        .getResultList();
```

## 📌 Fetch Join (성능 최적화)

- SQL의 JOIN과 유사하지만, 객체 그래프를 한 번에 조회할 때 사용
- Lazy Loading으로 인한 N+1 문제를 해결하기 위해 사용

### 🔹 JOIN vs Fetch Join 비교

```java
// Lazy Loading
SELECT m.*, t.* FROM member m
JOIN Team t ON m.team_id = t.id;

// Fetch Join
SELECT m, t FROM Member m Join Fetch m.team;
```

- Fetch Join을 사용하면 즉시 연관된 엔티티를 가져올 수 있음
- 성능 최적화를 위해 N + 1 문제를 방지하는 핵심 기술

## 📌 N + 1 문제

- `SELECT * FROM member` 실행 시
  - 각 멤버의 `team`을 조회하는 추가 쿼리가 실행됨
  - 총 1 + N 번의 쿼리가 실행되는 문제

### 🔹 해결방법

1. Fetch Join 사용 (JOIN FETCH 활용)
2. EntityGraph 사용 (`@EntityGraph`)
3. Batch Size 조정 (`@BatchSize`)

## 📌 Lazy Loading vs Eager Loading

### 🔹 Lazy Loading (지연 로딩)

- **필요한 때만** 데이터를 조회하는 방식
- 연관된 엔티티를 사용할 때 실제로 SQL이 실행됨
- 성능 최적화에 유리하지만, N + 1 문제 발생할 수 있음

```java
@Entity
public class Member {
    @ManyToOne(fetch = FetchType.LAZY) // 기본값
    private Team team;
}
```

- `@ManyToOne(fetch = FetchType.LAZY)`: 연관된 `team` 객체는 사용될 때만 쿼리 실행
- 여러 개의 `Member`를 조회하면 각 `team`을 조회하는 추가 쿼리가 실행됨

### 🔹Eager Loading (즉시 로딩)

- 즉시 모든 연관 데이터를 가져옴 (`JOIN` 사용)
- 쿼리 개수는 줄지만, 불필요한 데이터가 함께 로딩될 수 있음

```java
@Entity
public class Member {
  @ManyToOne(fetch = FetchType.EAGER) // 즉시 로딩
  private Team team;
}
```

- 즉시 `JOIN`을 사용하여 `team`을 함께 조회
- 모든 연관 데이터를 가져오기 때문에 성능 저하 가능성
- `FetchType.EAGER`는 항상 조인하여 데이터를 가져옴 (제어 불가)

## 🔗 참고자료

- [Spring Data JPA 공식 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Boot 공식 문서](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Hibernate 공식 문서](https://hibernate.org/orm/documentation/6.6/)
- [Spring Boot + JPA 가이드](https://spring.io/guides/gs/accessing-data-jpa/)
