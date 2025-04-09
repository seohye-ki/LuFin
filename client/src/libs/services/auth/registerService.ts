import axiosInstance from '../axios';

interface RegisterRequest {
  email: string;
  password: string;
  name: string;
  role: 'STUDENT' | 'TEACHER';
  secondaryPassword: string;
  profileImage: string;
}

interface RegisterResponse {
  isSuccess: boolean;
  data?: {
    email: string;
    role: string;
  };
  code?: string;
  message?: string;
}

interface EmailCheckResponse {
  isSuccess: boolean;
  data?: {
    email: string;
  };
  code?: string;
  message?: string;
}

export const registerService = {
  /**
   * 회원가입 API
   */
  register: async (data: RegisterRequest): Promise<RegisterResponse> => {
    const profileImage = `https://api.dicebear.com/9.x/bottts-neutral/svg?seed=${encodeURIComponent(data.email)}`;
    const response = await axiosInstance.post('/register', { ...data, profileImage });
    return response.data;
  },

  /**
   * 이메일 중복 확인
   */
  checkEmail: async (email: string): Promise<EmailCheckResponse> => {
    const response = await axiosInstance.get(`/register/emails`, {
      params: { email },
    });
    return response.data;
  },
};
