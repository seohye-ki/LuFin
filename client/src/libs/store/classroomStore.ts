import { create } from 'zustand';
import {
  classroomService,
  Classroom,
  CreateClassroomRequest,
  UpdateClassroomRequest,
} from '../services/classroom/classroomService';
import useAuthStore from './authStore';

interface ClassroomStore {
  classrooms: Classroom[];
  isLoading: boolean;
  error: string | null;
  // Actions
  fetchClassrooms: () => Promise<void>;
  createClassroom: (data: CreateClassroomRequest) => Promise<void>;
  updateClassroom: (data: UpdateClassroomRequest) => Promise<void>;
  deleteClassroom: (classId: number) => Promise<void>;
  joinClassroom: (code: string) => Promise<void>;
  reset: () => void;
}

const useClassroomStore = create<ClassroomStore>((set) => ({
  classrooms: [],
  isLoading: false,
  error: null,

  fetchClassrooms: async () => {
    try {
      set({ isLoading: true, error: null });
      const response = await classroomService.getClassrooms();
      if (response.isSuccess) {
        set({ classrooms: response.data });
      }
    } catch (err) {
      console.error('Failed to fetch classrooms:', err);
      set({ error: '클래스룸 목록을 불러오는데 실패했습니다.' });
    } finally {
      set({ isLoading: false });
    }
  },

  createClassroom: async (data: CreateClassroomRequest) => {
    try {
      set({ isLoading: true, error: null });
      const response = await classroomService.createClassroom(data);
      if (response.isSuccess) {
        // 새로운 토큰 저장
        const { token, classInfo } = response.data;
        useAuthStore.getState().setTokens(token.accessToken, token.refreshToken);

        // 클래스룸 목록 업데이트
        set((state) => ({
          classrooms: [...state.classrooms, classInfo],
        }));
      }
    } catch (error) {
      set({ error: '클래스룸 생성에 실패했습니다.' });
      throw error;
    } finally {
      set({ isLoading: false });
    }
  },

  updateClassroom: async (data: UpdateClassroomRequest) => {
    try {
      set({ isLoading: true, error: null });
      const response = await classroomService.updateClassroom(data);
      if (response.isSuccess) {
        // 클래스룸 목록 업데이트
        set((state) => ({
          classrooms: state.classrooms.map((classroom) =>
            classroom.classId === data.classId ? response.data : classroom,
          ),
        }));
      }
    } catch (error) {
      set({ error: '클래스룸 수정에 실패했습니다.' });
      throw error;
    } finally {
      set({ isLoading: false });
    }
  },

  deleteClassroom: async (classId: number) => {
    try {
      set({ isLoading: true, error: null });
      await classroomService.deleteClassroom(classId);
      // 클래스룸 목록에서 제거
      set((state) => ({
        classrooms: state.classrooms.filter((classroom) => classroom.classId !== classId),
      }));
    } catch (error) {
      set({ error: '클래스룸 삭제에 실패했습니다.' });
      throw error;
    } finally {
      set({ isLoading: false });
    }
  },

  joinClassroom: async (code: string) => {
    try {
      set({ isLoading: true, error: null });
      const response = await classroomService.joinClassroom(code);
      if (response.isSuccess) {
        // 새로운 토큰 저장
        const { token, classInfo } = response.data;
        useAuthStore.getState().setTokens(token.accessToken, token.refreshToken);

        // 클래스룸 목록 업데이트
        set((state) => ({
          classrooms: [...state.classrooms, classInfo],
        }));
      }
    } catch (error) {
      set({ error: '클래스룸 참여에 실패했습니다.' });
      throw error;
    } finally {
      set({ isLoading: false });
    }
  },

  reset: () => {
    set({ classrooms: [], isLoading: false, error: null });
  },
}));

export default useClassroomStore;
