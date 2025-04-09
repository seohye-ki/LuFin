import { create } from 'zustand';
import {
  classroomService,
  Classroom,
  CreateClassroomRequest,
  UpdateClassroomRequest,
} from '../services/classroom/classroomService';
import useAuthStore from './authStore';
import { tokenUtils } from '../services/axios';

interface ClassroomStore {
  classrooms: Classroom[];
  currentClassId: number | null;
  currentClassName: string | null;
  classCode: string | null;
  isLoading: boolean;
  error: string | null;
  // Actions
  fetchClassrooms: () => Promise<void>;
  createClassroom: (data: CreateClassroomRequest) => Promise<void>;
  updateClassroom: (data: UpdateClassroomRequest) => Promise<void>;
  deleteClassroom: (classId: number) => Promise<void>;
  joinClassroom: (code: string) => Promise<void>;
  enterClassCode: (code: string) => Promise<void>;
  changeCurrentClass: (classId: number, className?: string) => Promise<void>;
  getCurrentClassroom: () => Classroom | null;
  getClassCode: () => Promise<void>;
  reset: () => void;
}

const useClassroomStore = create<ClassroomStore>((set, get) => ({
  classrooms: [],
  currentClassId: null,
  currentClassName: null,
  classCode: null,
  isLoading: false,
  error: null,

  fetchClassrooms: async () => {
    try {
      set({ isLoading: true, error: null });
      const response = await classroomService.getClassrooms();
      if (response.isSuccess) {
        const classrooms = response.data;
        // 현재 클래스 ID 저장
        const currentClassIdStr = tokenUtils.getToken('classId');
        const currentClassId = currentClassIdStr ? parseInt(currentClassIdStr, 10) : null;

        // 현재 클래스 이름 찾기
        let currentClassName = null;
        if (currentClassId) {
          const currentClass = classrooms.find((c) => c.classId === currentClassId);
          if (currentClass) {
            currentClassName = currentClass.name;
          }
        }

        set({
          classrooms: classrooms,
          currentClassId: currentClassId || (classrooms.length > 0 ? classrooms[0].classId : null),
          currentClassName: currentClassName || (classrooms.length > 0 ? classrooms[0].name : null),
        });
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
        useAuthStore.getState().setTokens(token.accessToken, token.refreshToken, token.classId);

        // 클래스룸 목록 업데이트
        set((state) => ({
          classrooms: [...state.classrooms, classInfo],
          currentClassId: token.classId,
          currentClassName: classInfo.name,
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
        // 현재 선택된 클래스가 삭제된 경우 첫 번째 클래스로 변경
        currentClassId:
          state.currentClassId === classId
            ? state.classrooms.length > 1
              ? state.classrooms.find((c) => c.classId !== classId)?.classId || null
              : null
            : state.currentClassId,
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
        useAuthStore.getState().setTokens(token.accessToken, token.refreshToken, token.classId);

        // 클래스룸 목록 업데이트
        set((state) => ({
          classrooms: [...state.classrooms, classInfo],
          currentClassId: token.classId,
          currentClassName: classInfo.name,
        }));
      }
    } catch (error) {
      set({ error: '클래스룸 참여에 실패했습니다.' });
      throw error;
    } finally {
      set({ isLoading: false });
    }
  },

  enterClassCode: async (code: string) => {
    try {
      set({ isLoading: true, error: null });
      const response = await classroomService.enterClassCode(code);
      if (response.isSuccess) {
        // 새로운 액세스 토큰 저장
        const { accessToken } = response.data;
        const currentRefreshToken = tokenUtils.getToken('refreshToken');
        const currentClassId = tokenUtils.getToken('classId');

        if (currentRefreshToken && currentClassId) {
          useAuthStore
            .getState()
            .setTokens(accessToken, currentRefreshToken, parseInt(currentClassId, 10));
        }

        // 클래스룸 목록 다시 불러오기
        await get().fetchClassrooms();
      }
    } catch (error) {
      set({ error: '클래스 코드 입력에 실패했습니다.' });
      throw error;
    } finally {
      set({ isLoading: false });
    }
  },

  changeCurrentClass: async (classId: number, className?: string) => {
    try {
      set({ isLoading: true, error: null });

      // 사용자 역할 확인
      const userRole = tokenUtils.getToken('userRole');

      // 교사인 경우에만 API 호출
      if (userRole?.toUpperCase() === 'TEACHER') {
        const response = await classroomService.changeCurrentClass(classId);
        if (!response.isSuccess) {
          throw new Error('클래스 전환에 실패했습니다.');
        }
      }

      // 클래스 ID 저장 (API 성공 여부와 관계없이 로컬 상태 업데이트)
      tokenUtils.setToken('classId', classId.toString());

      // 현재 선택된 클래스 ID와 이름 업데이트
      set({
        currentClassId: classId,
        currentClassName:
          className || get().classrooms.find((c) => c.classId === classId)?.name || null,
      });

      console.log(`클래스 변경 완료: ${classId} (${userRole})`);
    } catch (error) {
      console.error('클래스 전환 오류:', error);
      set({ error: '클래스 전환에 실패했습니다.' });
      throw error;
    } finally {
      set({ isLoading: false });
    }
  },

  getCurrentClassroom: () => {
    const { classrooms, currentClassId } = get();
    return classrooms.find((classroom) => classroom.classId === currentClassId) || null;
  },

  getClassCode: async () => {
    try {
      set({ isLoading: true, error: null });
      const response = await classroomService.getClassCode();
      if (response.isSuccess) {
        set({ classCode: response.data.classCode });
      }
    } catch (error) {
      console.error('Failed to fetch class code:', error);
      set({ error: '클래스 코드 조회에 실패했습니다.' });
    } finally {
      set({ isLoading: false });
    }
  },

  reset: () => {
    tokenUtils.removeToken('classId');
    set({
      classrooms: [],
      currentClassId: null,
      currentClassName: null,
      isLoading: false,
      error: null,
    });
  },
}));

export default useClassroomStore;
