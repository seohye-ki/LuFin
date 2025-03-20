# **LuFin Backend**

LuFin 백엔드는 금융 교육을 위한 **Spring Boot 기반의 REST API 서버**입니다.  
이 문서는 프로젝트의 폴더 구조 및 각 디렉토리의 역할을 설명합니다.

---

## **📂 프로젝트 폴더 구조**

### **1. 최상위 디렉토리**
- `gradle/`, `.gradle/` - Gradle 관련 설정 파일
- `build/` - 빌드된 결과물이 저장되는 폴더 (Git에 포함되지 않음)
- `src/` - 메인 코드가 위치한 디렉토리
- `resources/` - 환경 설정 파일 및 정적 파일 저장
- `test/` - 테스트 코드 저장

---

### **2. 주요 폴더 및 역할**
#### **📌 `src/main/java/com/lufin/server/`**
- **`common/`** - 프로젝트에서 공통적으로 사용되는 기능
    - `constants/` - 상수 및 Enum (예: `ErrorCode`)
    - `exception/` - 예외 처리 클래스 (예: `CustomException`)
    - `interceptor/` - 요청 가로채기 필터 (예: `JWT 인증`)
    - `utils/` - 공통적으로 사용하는 유틸 클래스 (예: `DateUtil`)

- **`config/`** - 프로젝트 설정 관련 클래스
    - 예: `SecurityConfig`, `WebMvcConfig`

- **`member/`** - 회원 도메인 관련 폴더
    - `controller/` - 회원 관련 API 컨트롤러
    - `service/` - 회원 관련 비즈니스 로직 처리
    - `repository/` - 데이터베이스 접근을 담당하는 JPA Repository
    - `domain/` - 회원 관련 엔티티 클래스

---

### **3. 리소스 및 환경 설정**
#### **📌 `src/main/resources/`**
- `application.yml` - 프로젝트 환경 설정 파일 (DB, Redis 등)
- `static/` - 정적 리소스 (필요 시 추가)
- `templates/` - HTML 템플릿 파일 (필요 시 추가)

---

### **4. 테스트 코드**
#### **📌 `src/test/java/com/lufin/server/`**
- 각 도메인(`member/`, `common/`)별 테스트 코드 작성
- 서비스 및 컨트롤러 단위 테스트 진행

---

## **🚀 프로젝트 실행 방법**
1. **환경 변수 설정** (`application.yml` 작성)
2. **Gradle 빌드 및 실행**
   ```sh
   ./gradlew build
   ./gradlew bootRun
   ```
3. **서버 실행 확인**
    - 기본 포트: `http://localhost:8080`
