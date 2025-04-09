import { create } from 'zustand';
import { AuthService, LoginRequest } from '../services/auth/authService';
import { fileService } from '../services/file/fileService';

// 인증 스토어의 상태 타입 정의
interface AuthState {
  // 상태
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
  errorCode: string | null;
  userRole: string | null;
  userId: number | null;
  userName: string | null;
  userProfileImage: string | null;
  totalAsset: number;
  activeClassId: number | null;

  // 액션
  login: (credentials: LoginRequest) => Promise<{
    success: boolean;
    message?: string;
    code?: string;
    role?: string;
    redirectPath?: string;
  }>;
  logout: () => void;
  clearError: () => void;
  setTokens: (accessToken: string, refreshToken: string, classId?: number) => void;
  // 상태 selector 헬퍼 함수
  getAuthStatus: () => {
    isAuthenticated: boolean;
    isLoading: boolean;
    error: string | null;
    errorCode: string | null;
    userRole: string | null;
  };
}

/**
 * 인증 상태를 관리하는 Zustand 스토어
 * 로그인, 로그아웃, 인증 상태 관리 등의 기능을 제공합니다.
 */
const useAuthStore = create<AuthState>((set, get) => ({
  // 초기 상태
  isAuthenticated: AuthService.isAuthenticated(),
  isLoading: false,
  error: null,
  errorCode: null,
  userRole: AuthService.getUserRole(),
  userId: null,
  userName: null,
  userProfileImage: null,
  totalAsset: 0,
  activeClassId: null,

  /**
   * 로그인 처리
   * @param credentials 로그인 정보 (이메일, 비밀번호)
   * @returns 로그인 결과 객체
   */
  login: async (credentials: LoginRequest) => {
    // 로딩 상태로 변경
    set({ isLoading: true, error: null, errorCode: null });

    try {
      // AuthService를 통한 로그인 처리
      const result = await AuthService.login(credentials);

      if (result.success) {
        // 로그인 성공 시 상태 업데이트
        set({
          isAuthenticated: true,
          isLoading: false,
          userRole: result.role || null,
          error: null,
          errorCode: null,
          userName: result.name,
          userProfileImage: await fileService.getImageUrl(result.profileImage as string),
          totalAsset: result.totalAsset,
          activeClassId: result.classId,
        });

        return {
          success: true,
          role: result.role,
          redirectPath: result.redirectPath,
        };
      } else {
        // 로그인 실패 시 에러 상태 업데이트
        set({
          isLoading: false,
          error: result.message || null,
          errorCode: result.code || null,
          isAuthenticated: false,
        });

        return result;
      }
    } catch {
      // 예외 발생 시 에러 처리
      const errorMessage = '로그인 중 오류가 발생했습니다.';
      set({
        isLoading: false,
        error: errorMessage,
        errorCode: null,
        isAuthenticated: false,
      });

      return { success: false, message: errorMessage };
    }
  },

  /**
   * 로그아웃 처리
   */
  logout: () => {
    // 서비스를 통한 로그아웃 처리
    AuthService.logout();

    // 상태 초기화
    set({
      isAuthenticated: false,
      userRole: null,
      error: null,
      errorCode: null,
    });
  },

  /**
   * 에러 상태 초기화
   */
  clearError: () => set({ error: null, errorCode: null }),

  /**
   * 토큰 설정
   * @param accessToken 새로운 액세스 토큰
   * @param refreshToken 새로운 리프레시 토큰
   * @param classId 현재 클래스 ID (선택적)
   */
  setTokens: (accessToken: string, refreshToken: string, classId?: number) => {
    AuthService.setTokens(accessToken, refreshToken, classId);
    set({
      isAuthenticated: true,
    });
  },

  /**
   * 현재 인증 상태 정보를 가져오는 헬퍼 함수
   * @returns 인증 상태 객체
   */
  getAuthStatus: () => {
    const { isAuthenticated, isLoading, error, errorCode, userRole } = get();
    return { isAuthenticated, isLoading, error, errorCode, userRole };
  },
}));

export default useAuthStore;
