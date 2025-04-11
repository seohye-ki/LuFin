import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import { AuthService, LoginRequest } from '../services/auth/authService';
import { tokenUtils } from '../services/axios';

interface AuthState {
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
  errorCode: string | null;
  userRole: string | null;
  userId: number | null;
  userName: string | null;
  userProfileImage: string | null;
  totalAsset: number;

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
  getAuthStatus: () => {
    isAuthenticated: boolean;
    isLoading: boolean;
    error: string | null;
    errorCode: string | null;
    userRole: string | null;
  };
}

const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      isAuthenticated: AuthService.isAuthenticated(),
      isLoading: false,
      error: null,
      errorCode: null,
      userRole: AuthService.getUserRole(),
      userId: parseInt(tokenUtils.getToken('userId') || '0'),
      userName: null,
      userProfileImage: null,
      totalAsset: 0,

      login: async (credentials: LoginRequest) => {
        set({ isLoading: true, error: null, errorCode: null });

        try {
          const result = await AuthService.login(credentials);

          if (result.success) {
            set({
              isAuthenticated: true,
              isLoading: false,
              userRole: result.role || null,
              userId: result.memberId || null,
              error: null,
              errorCode: null,
              userName: result.name,
              userProfileImage: result.profileImage,
              totalAsset: result.totalAsset,
            });

            return {
              success: true,
              role: result.role,
              redirectPath: result.redirectPath,
            };
          } else {
            set({
              isLoading: false,
              error: result.message || null,
              errorCode: result.code || null,
              isAuthenticated: false,
            });

            return result;
          }
        } catch {
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

      logout: () => {
        AuthService.logout();
        tokenUtils.removeToken('userId');
        set({
          isAuthenticated: false,
          userRole: null,
          error: null,
          errorCode: null,
          userId: null,
          userName: null,
          userProfileImage: null,
          totalAsset: 0,
        });
      },

      clearError: () => set({ error: null, errorCode: null }),

      setTokens: (accessToken: string, refreshToken: string, classId?: number) => {
        AuthService.setTokens(accessToken, refreshToken, classId);
        set({ isAuthenticated: true });
      },

      getAuthStatus: () => {
        const { isAuthenticated, isLoading, error, errorCode, userRole } = get();
        return { isAuthenticated, isLoading, error, errorCode, userRole };
      },
    }),
    {
      name: 'auth-store',
      storage: createJSONStorage(() => sessionStorage),
      partialize: (state) => ({
        isAuthenticated: state.isAuthenticated,
        userRole: state.userRole,
        userId: state.userId,
        userName: state.userName,
        userProfileImage: state.userProfileImage,
        totalAsset: state.totalAsset,
      }),
    },
  ),
);

export default useAuthStore;
