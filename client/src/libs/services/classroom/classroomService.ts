import useAuthStore from '../../store/authStore';
import useClassroomStore from '../../store/classroomStore';
import axiosInstance, { tokenUtils } from '../axios';

// ---------------------
// 타입 정의
// ---------------------

export interface Classroom {
  classId: number;
  name: string;
  school: string;
  year: number;
  grade: number;
  classGroup: number;
  code: string;
  memberCount: number;
  balance: number;
  key: string | null;
}

export interface CreateClassroomRequest {
  name: string;
  school: string;
  grade: number;
  classGroup: number;
  key: string | null;
}

export interface UpdateClassroomRequest {
  classId: number;
  name: string;
  school: string;
  grade: number;
  classGroup: number;
  key: string | null;
}

export interface CreateClassroomResponse {
  token: {
    accessToken: string;
    refreshToken: string;
    role: string;
    classId: number;
    accountNumber: string;
  };
  classInfo: Classroom;
}

export interface JoinClassroomResponse {
  token: {
    accessToken: string;
    refreshToken: string;
    role: string;
    classId: number;
    accountNumber: string;
  };
  classInfo: Classroom;
}

export interface ClassCodeResponse {
  classCode: string;
}

export interface ClassCodeEntryResponse {
  token: {
    accessToken: string;
    refreshToken: string;
    classId: number;
  };
}

export interface ChangeClassToken {
  accessToken: string;
  refreshToken: string;
  role: string;
}

export interface ChangeCurrentClassResponse {
  token: ChangeClassToken;
  classInfo: Classroom;
}

export interface ApiResponse<T> {
  isSuccess: boolean;
  data: T;
}

// ---------------------
// API 함수
// ---------------------

export const classroomService = {
  // 클래스 목록 조회
  getClassrooms: async (): Promise<ApiResponse<Classroom[]>> => {
    const response = await axiosInstance.get<ApiResponse<Classroom[]>>('/classes');
    return response.data;
  },

  // 클래스 생성
  createClassroom: async (
    data: CreateClassroomRequest,
  ): Promise<ApiResponse<CreateClassroomResponse>> => {
    const response = await axiosInstance.post<ApiResponse<CreateClassroomResponse>>(
      '/classes',
      data,
    );
    return response.data;
  },

  // 클래스 수정
  updateClassroom: async (data: UpdateClassroomRequest): Promise<ApiResponse<Classroom>> => {
    const response = await axiosInstance.patch<ApiResponse<Classroom>>('/classes/current', data);
    return response.data;
  },

  // 클래스 삭제
  deleteClassroom: async (classId: number): Promise<void> => {
    await axiosInstance.delete('/classes', {
      data: { classId },
    });
  },

  // 활성화된 클래스 변경
  changeCurrentClass: async (classId: number): Promise<ApiResponse<ChangeCurrentClassResponse>> => {
    const response = await axiosInstance.post<ApiResponse<ChangeCurrentClassResponse>>(
      '/classes/current/change',
      { classId },
    );

    const { token } = response.data.data;

    tokenUtils.setToken('accessToken', token.accessToken);
    tokenUtils.setToken('refreshToken', token.refreshToken);
    tokenUtils.setToken('userRole', token.role);

    return response.data;
  },

  // 현재 클래스 코드 조회 (교사용)
  getClassCode: async (): Promise<ApiResponse<ClassCodeResponse>> => {
    const response =
      await axiosInstance.get<ApiResponse<ClassCodeResponse>>('/classes/current/code');
    return response.data;
  },

  // 학생 클래스 코드 입력
  joinClass: async (code: string): Promise<ApiResponse<ClassCodeEntryResponse>> => {
    const response = await axiosInstance.post<ApiResponse<ClassCodeEntryResponse>>('/auth/class', {
      code,
    });

    const { accessToken, refreshToken, classId } = response.data.data.token;

    const authStore = useAuthStore.getState();
    authStore.setTokens(accessToken, refreshToken, classId);
    return response.data;
  },
};
