# Axios 사용 가이드

이 가이드는 프로젝트의 HTTP 요청을 처리하기 위한 `axios.ts` 사용법을 설명합니다.

## 기본 설정

우리 프로젝트의 `axios.ts`는 다음과 같은 기능을 자동으로 제공합니다:

- API 기본 URL 설정 (환경변수 `VITE_API_URL` 사용)
- 요청 타임아웃 (30초)
- 인증 토큰 자동 처리
- 에러 핸들링

## 일반 axios와의 차이점

### 1. 일반 axios 사용시

```typescript
import axios from 'axios';

const getUsers = async () => {
  try {
    // 매번 baseURL을 직접 입력해야 함
    const response = await axios.get('http://api.example.com/users', {
      // 매번 토큰을 직접 헤더에 포함시켜야 함
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
      },
    });
    return response.data;
  } catch (error) {
    // 에러 처리를 매번 직접 구현해야 함
    if (error.response?.status === 401) {
      // 토큰 만료 처리
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
    }
    throw error;
  }
};
```

### 2. 우리의 axiosInstance 사용시

```typescript
import axiosInstance from '@/libs/services/axios';

const getUsers = async () => {
  try {
    // baseURL과 토큰이 자동으로 처리됨
    const response = await axiosInstance.get('/users');
    return response.data;
  } catch (error) {
    // 401, 403 등의 에러가 자동으로 처리됨
    console.error('사용자 정보 조회 실패:', error);
    throw error;
  }
};
```

### 주요 이점

1. **코드 간결성**

   - baseURL을 매번 입력할 필요가 없음
   - 인증 토큰 처리 로직 중복 제거
   - 일관된 에러 처리

2. **유지보수성**

   - API 엔드포인트 변경 시 한 곳만 수정
   - 인증 로직 변경 시 한 곳만 수정
   - 에러 처리 로직 통합 관리

3. **보안성**
   - 토큰 관리 로직 중앙화
   - 일관된 인증 처리
   - 토큰 만료 시 자동 로그아웃

## 사용 방법

### 1. axios 인스턴스 임포트

```typescript
import axiosInstance from '@/libs/services/axios';
```

### 2. GET 요청 예시

```typescript
// 기본적인 GET 요청
const getUsers = async () => {
  try {
    const response = await axiosInstance.get('/users');
    return response.data;
  } catch (error) {
    console.error('사용자 정보 조회 실패:', error);
    throw error;
  }
};

// Query 파라미터를 포함한 GET 요청
const searchUsers = async (searchTerm: string) => {
  try {
    const response = await axiosInstance.get('/users/search', {
      params: { query: searchTerm },
    });
    return response.data;
  } catch (error) {
    console.error('사용자 검색 실패:', error);
    throw error;
  }
};
```

### 3. POST 요청 예시

```typescript
// 기본적인 POST 요청
const createUser = async (userData: UserData) => {
  try {
    const response = await axiosInstance.post('/users', userData);
    return response.data;
  } catch (error) {
    console.error('사용자 생성 실패:', error);
    throw error;
  }
};

// FormData를 사용한 POST 요청
const uploadFile = async (file: File) => {
  const formData = new FormData();
  formData.append('file', file);

  try {
    const response = await axiosInstance.post('/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  } catch (error) {
    console.error('파일 업로드 실패:', error);
    throw error;
  }
};
```

## 주요 특징

1. **토큰 자동 처리**

   - 로그인 후 저장된 토큰이 자동으로 요청 헤더에 포함됩니다.
   - 별도로 Authorization 헤더를 설정할 필요가 없습니다.

2. **에러 처리**
   - 401 에러: 자동으로 로그아웃 처리 및 로그인 페이지로 리다이렉트
   - 403 에러: 권한 없음 에러 처리
   - 404 에러: 리소스를 찾을 수 없음
   - 500 에러: 서버 에러 처리

## 토큰 관리

토큰은 `tokenUtils`를 통해 관리됩니다:

```typescript
import { tokenUtils } from '@/libs/services/axios';

// 토큰 저장
tokenUtils.setToken('accessToken', 'your-token-here');

// 토큰 조회
const token = tokenUtils.getToken('accessToken');

// 토큰 삭제
tokenUtils.removeToken('accessToken');
```

## 주의사항

1. 에러 처리는 try-catch 구문을 사용하여 적절히 처리해주세요.
2. 파일 업로드시 Content-Type을 'multipart/form-data'로 설정해야 합니다.
3. 토큰 관련 작업은 제공된 `tokenUtils`를 사용해주세요.
