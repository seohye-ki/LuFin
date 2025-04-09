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
    name: string;
    profileImage: string;
    totalAsset: number;
  };
  code?: string;
  message?: string;
}

interface ErrorResponse {
  response: {
    data: AuthResponse;
  };
}

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
      const response = await axiosInstance.post<AuthResponse>('/auth/login', credentials);

      if (response.data.isSuccess && response.data.data) {
        // 토큰 저장
        const { accessToken, refreshToken, role, name, profileImage, totalAsset } =
          response.data.data;
        tokenUtils.setToken('accessToken', accessToken);
        tokenUtils.setToken('refreshToken', refreshToken);
        tokenUtils.setToken('userRole', role);

        // 역할에 따른 리다이렉션 경로 결정
        const redirectPath = '/classroom';

        return {
          success: true,
          role: role,
          redirectPath,
          name,
          profileImage,
          totalAsset,
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

      if (error && typeof error === 'object' && 'response' in error) {
        const responseData = (error as ErrorResponse).response.data;
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
   * @param classId 선택된 클래스 ID (선택적)
   */
  setTokens: (accessToken: string, refreshToken: string, classId?: number) => {
    tokenUtils.setToken('accessToken', accessToken);
    tokenUtils.setToken('refreshToken', refreshToken);

    // classId가 제공된 경우에만 저장
    if (classId !== undefined) {
      tokenUtils.setToken('classId', classId.toString());
    }
  },
};
