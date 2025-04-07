import axios from 'axios';
import axiosInstance, { tokenUtils } from '../axios';

// 인터페이스 정의
export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  isSuccess: boolean;
  data?: {
    accessToken: string;
    refreshToken: string;
    role: string;
  };
  code?: string;
  message?: string;
}

// 환경 변수에서 로그인 엔드포인트 가져오기
const LOGIN_ENDPOINT = import.meta.env.VITE_AUTH_LOGIN_ENDPOINT || '/auth/login';

/**
 * 인증 관련 서비스 함수들
 * 비즈니스 로직과 API 호출을 담당
 */
export const AuthService = {
  /**
   * 로그인 처리
   * @param credentials 로그인 정보 (이메일, 비밀번호)
   * @returns 로그인 결과 객체
   */
  login: async (credentials: LoginRequest) => {
    try {
      const response = await axiosInstance.post<AuthResponse>(LOGIN_ENDPOINT, credentials);

      if (response.data.isSuccess && response.data.data) {
        // 토큰 저장
        const { accessToken, refreshToken, role } = response.data.data;
        tokenUtils.setToken('accessToken', accessToken);
        tokenUtils.setToken('refreshToken', refreshToken);
        tokenUtils.setToken('userRole', role);

        // 역할에 따른 리다이렉션 경로 결정
        const redirectPath = role.toUpperCase() === 'TEACHER' ? '/classroom/teacher' : '/classroom';

        return {
          success: true,
          role: role,
          redirectPath,
        };
      } else {
        return {
          success: false,
          code: response.data.code,
          message: response.data.message || '로그인에 실패했습니다.',
        };
      }
    } catch (error) {
      let errorMessage = '로그인 중 오류가 발생했습니다.';
      let errorCode = '';

      if (axios.isAxiosError(error) && error.response) {
        const responseData = error.response.data as AuthResponse;
        errorMessage = responseData.message || errorMessage;
        errorCode = responseData.code || '';
      }

      return { success: false, code: errorCode, message: errorMessage };
    }
  },

  /**
   * 로그아웃 처리
   */
  logout: () => {
    tokenUtils.removeToken('accessToken');
    tokenUtils.removeToken('refreshToken');
    tokenUtils.removeToken('userRole');
    window.location.href = '/login';
  },

  /**
   * 현재 인증 상태 확인
   * @returns 인증 여부
   */
  isAuthenticated: () => {
    return !!tokenUtils.getToken('accessToken');
  },

  /**
   * 사용자 역할 가져오기
   * @returns 사용자 역할(권한)
   */
  getUserRole: () => {
    return tokenUtils.getToken('userRole');
  },

  /**
   * 현재 사용자의 리다이렉션 경로 가져오기
   * @returns 리다이렉션 경로
   */
  getRedirectPath: () => {
    const role = tokenUtils.getToken('userRole');
    return role?.toUpperCase() === 'TEACHER' ? '/classroom/teacher' : '/classroom';
  },

  /**
   * 토큰 설정
   * @param accessToken 새로운 액세스 토큰
   * @param refreshToken 새로운 리프레시 토큰
   */
  setTokens: (accessToken: string, refreshToken: string) => {
    tokenUtils.setToken('accessToken', accessToken);
    tokenUtils.setToken('refreshToken', refreshToken);
  },
};
