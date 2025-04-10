import axios, { AxiosError } from 'axios';
import { hideGlobalAlert, showGlobalAlert } from '../store/alertStore';

/**
 * Axios 인스턴스 생성
 * - baseURL: API의 기본 URL을 설정합니다. 모든 요청의 prefix로 사용됩니다.
 * - timeout: 요청 제한 시간을 설정합니다 (30초).
 * - headers: 기본 헤더를 설정합니다.
 */

export const axiosInstance = axios.create({
  // 환경변수에서 API URL을 가져옵니다.
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
  transformResponse: (data) => {
    return JSON.parse(data);
  },
});

// 토큰 저장소 결정 (sessionStorage 또는 localStorage)
const TOKEN_STORAGE = import.meta.env.VITE_TOKEN_STORAGE || 'sessionStorage';

/**
 * 토큰 유틸리티 함수
 * - getToken: 저장소에서 토큰을 조회합니다.
 * - setToken: 저장소에 토큰을 저장합니다. (로그인 성공 시 호출)
 * - removeToken: 저장소에서 토큰을 삭제합니다. (로그아웃 시 호출)
 */
export const tokenUtils = {
  getToken: (key: string): string | null => {
    const token =
      TOKEN_STORAGE === 'localStorage' ? localStorage.getItem(key) : sessionStorage.getItem(key);
    ('');
    return token;
  },

  setToken: (key: string, value: string): void => {
    ('');
    if (TOKEN_STORAGE === 'localStorage') {
      localStorage.setItem(key, value);
    } else {
      sessionStorage.setItem(key, value);
    }
  },

  removeToken: (key: string): void => {
    ('');
    if (TOKEN_STORAGE === 'localStorage') {
      localStorage.removeItem(key);
    } else {
      sessionStorage.removeItem(key);
    }
  },
};

/**
 * 요청 인터셉터
 * 모든 HTTP 요청이 실행되기 전에 실행됩니다.
 *
 * [토큰 추가 프로세스]
 * 1. axiosInstance.get/post/put/delete 등의 메서드 호출
 * 2. 실제 HTTP 요청이 전송되기 전에 이 인터셉터가 자동으로 실행됨
 * 3. tokenUtils.getToken('accessToken')으로 저장된 토큰 조회
 * 4. 토큰이 존재하면 요청 헤더에 Authorization 값으로 추가
 * 5. 수정된 설정으로 실제 HTTP 요청 전송
 */
axiosInstance.interceptors.request.use(
  (config) => {
    // 토큰 가져오기
    const accessToken = tokenUtils.getToken('accessToken');

    // 토큰이 존재하면 Authorization 헤더에 추가합니다.
    if (accessToken && config.headers) {
      config.headers.Authorization = accessToken;
    }
    return config;
  },
  (error) => {
    // 요청 전에 에러가 발생하면 Promise.reject로 처리합니다.
    return Promise.reject(error);
  },
);

interface ErrorResponse {
  message?: string;
  code?: string;
}

/**
 * 응답 인터셉터
 * 모든 HTTP 응답을 처리하기 전에 실행됩니다.
 *
 * [토큰 관련 에러 처리]
 * 1. 401 에러 발생 시 (토큰 만료 또는 유효하지 않은 토큰)
 *    - 저장된 모든 토큰 삭제
 *    - 로그인 페이지로 리다이렉트
 * 2. 403 에러 발생 시 (권한 부족)
 *    - 접근 권한 없음 에러 로깅
 */

axiosInstance.interceptors.response.use(
  // 정상적인 응답 처리
  (response) => response,

  // 에러 응답 처리
  (error: AxiosError) => {
    const message = (error.response?.data as ErrorResponse)?.message || '알 수 없는 에러 발생';
    const isEmailCheckEndpoint = error.config?.url?.includes('/register/emails');

    // 이메일 중복 체크 API가 아닌 경우에만 전역 알림창 표시
    if (!isEmailCheckEndpoint) {
      showGlobalAlert('에러 발생', null, message, 'danger', {
        label: '확인',
        onClick: () => {
          hideGlobalAlert();
        },
        color: 'danger',
      });
    }

    if (error.response?.status === 401) {
      tokenUtils.removeToken('accessToken');
      tokenUtils.removeToken('refreshToken');
      tokenUtils.removeToken('userRole');
      window.location.href = '/login';
    }

    return Promise.reject(error);
  },
);

export default axiosInstance;
