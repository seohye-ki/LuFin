import { create } from 'zustand';
import {
  MissionRaw,
  MissionList,
  MissionCreateRequest,
  MissionUpdateRequest,
  ParticipationUserInfo,
} from '../../types/mission/mission';
import { missionService } from '../services/mission/missionService';

interface MissionState {
  // 상태
  missions: MissionList[];
  selectedMission: MissionRaw | null;
  isLoading: boolean;
  error: string | null;
  errorCode: string | null;

  // 액션
  getMissionList: () => Promise<{ success: boolean; message?: string; code?: string }>;
  getMissionDetail: (
    missionId: number,
  ) => Promise<{ success: boolean; message?: string; code?: string; mission?: MissionRaw }>;
  createMission: (
    mission: MissionCreateRequest,
  ) => Promise<{ success: boolean; missionId?: number; message?: string }>;
  updateMission: (mission: MissionUpdateRequest) => Promise<{ success: boolean; message?: string }>;
  applyMission: (missionId: number) => Promise<{ success: boolean; message?: string }>;
  deleteMission: (missionId: number) => Promise<{ success: boolean; message?: string }>;
  requestReview: (participationId: number) => Promise<{ success: boolean; message?: string }>;
  getMissionWithParticipants: (
    missionId: number,
  ) => Promise<{ success: boolean; message?: string }>;
  getParticipationList: (
    missionId: number,
  ) => Promise<{ success: boolean; message?: string; participations?: ParticipationUserInfo[] }>;

  // 상태 selector
  getMissionStatus: () => {
    missions: MissionList[];
    selectedMission: MissionRaw | null;
    isLoading: boolean;
    error: string | null;
    errorCode: string | null;
  };
}

const useMissionStore = create<MissionState>((set, get) => ({
  // 초기 상태
  missions: [],
  selectedMission: null,
  isLoading: false,
  error: null,
  errorCode: null,

  /**
   * 미션 목록 조회
   */
  getMissionList: async () => {
    set({ isLoading: true, error: null, errorCode: null });

    try {
      const result = await missionService.getMissionList();

      if (result.success) {
        console.log('미션 목록 조회 성공:', result.missions);
        set({
          missions: result.missions || [],
          isLoading: false,
        });
      } else {
        console.log('미션 목록 조회 실패:', result.message);
        set({
          isLoading: false,
          error: result.message || null,
          errorCode: result.code || null,
        });
      }

      return result;
    } catch (error) {
      console.error('미션 목록 조회 에러:', error);
      const message = '미션 목록을 불러오는데 실패했습니다.';
      set({
        isLoading: false,
        error: message,
        errorCode: null,
      });
      return {
        success: false,
        message,
      };
    }
  },
  /**
   * 미션 상세 조회
   */
  getMissionDetail: async (missionId: number) => {
    set({ isLoading: true, error: null, errorCode: null });
    console.log('미션 상세 조회 시작:', missionId);

    try {
      const result = await missionService.getMissionDetail(missionId);

      if (result.success) {
        console.log('미션 상세 조회 성공:', result.mission);
        set({
          selectedMission: result.mission,
          isLoading: false,
          error: null,
          errorCode: null,
        });
        return { success: true, mission: result.mission };
      } else {
        console.log('미션 상세 조회 실패:', result.message);
        set({
          isLoading: false,
          error: result.message || null,
          errorCode: result.code || null,
        });
        return { success: false, message: result.message, code: result.code };
      }
    } catch (error) {
      console.error('미션 상세 조회 에러:', error);
      const message = '미션 상세를 불러오는데 실패했습니다.';
      set({
        isLoading: false,
        error: message,
        errorCode: null,
      });
      return { success: false, message };
    }
  },
  /**
   * 미션에 참여한 유저 정보 조회
   */
  getMissionWithParticipants: async (missionId: number) => {
    set({ isLoading: true });
    const [missionRes, participationRes] = await Promise.all([
      missionService.getMissionDetail(missionId),
      missionService.getParticipationList(missionId),
    ]);
    if (missionRes.success && participationRes.success) {
      set({
        selectedMission: missionRes.mission,
        isLoading: false,
      });
      return { success: true };
    } else {
      set({
        isLoading: false,
      });
      return { success: false, message: '미션 상세 조회에 실패했습니다.' };
    }
  },
  /**
   * 미션 생성
   */
  createMission: async (
    mission: MissionCreateRequest,
  ): Promise<{ success: boolean; missionId?: number; message?: string }> => {
    set({ isLoading: true, error: null, errorCode: null });

    try {
      const result = await missionService.createMission(mission);

      if (result.success) {
        console.log('미션 생성 성공:', result.missionId);
        set({
          isLoading: false,
        });
        return { success: true, missionId: result.missionId };
      } else {
        console.log('미션 생성 실패:', result.message);
        set({
          isLoading: false,
          error: result.message || null,
        });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error('미션 생성 에러:', error);
      const message = '미션 생성에 실패했습니다.';
      set({
        isLoading: false,
        error: message,
      });
      return { success: false, message };
    }
  },
  /**
   * 미션 수정
   */
  updateMission: async (
    mission: MissionUpdateRequest,
  ): Promise<{ success: boolean; message?: string }> => {
    set({ isLoading: true, error: null, errorCode: null });

    try {
      const result = await missionService.updateMission(mission);

      if (result.success) {
        console.log('미션 수정 성공');
        set({ isLoading: false });
        return { success: true, message: result.message };
      } else {
        console.log('미션 수정 실패:', result.message);
        set({ isLoading: false, error: result.message || null });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error('미션 수정 에러:', error);
      const message = '미션 수정에 실패했습니다.';
      set({ isLoading: false, error: message });
      return { success: false, message };
    }
  },
  /**
   * 미션 삭제
   */
  deleteMission: async (missionId: number): Promise<{ success: boolean; message?: string }> => {
    set({ isLoading: true });
    try {
      const result = await missionService.deleteMission(missionId);
      if (result.success) {
        console.log('미션 삭제 성공');
        await get().getMissionList();
        set({ isLoading: false });
        return { success: true };
      } else {
        console.log('미션 삭제 실패:', result.message);
        set({
          isLoading: false,
          error: result.message || null,
          errorCode: result.code || null,
        });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error('미션 삭제 에러:', error);
      const message = '미션 삭제에 실패했습니다.';
      set({ isLoading: false, error: message });
      return { success: false, message };
    }
  },
  /**
   * 미션 참여자 목록 조회
   */
  getParticipationList: async (missionId: number) => {
    const result = await missionService.getParticipationList(missionId);
    return result;
  },
  /**
   * 미션 신청
   */
  applyMission: async (missionId: number) => {
    const result = await missionService.applyMission(missionId);
    return result;
  },
  /**
   * 미션 리뷰 요청
   */
  requestReview: async (participationId: number) => {
    const result = await missionService.requestReview(participationId);
    return result;
  },
  /**
   * 미션 상태 변경
   */
  changeMissionStatus: async (
    participationId: number,
    status: 'SUCCESS' | 'FAILED' | 'REJECTED' | 'CHECKING',
  ) => {
    const result = await missionService.changeMissionStatus(participationId, status);
    return result;
  },
  getMissionStatus: () => {
    const { missions, selectedMission, isLoading, error, errorCode } = get();
    return { missions, selectedMission, isLoading, error, errorCode };
  },
  resetMissionStatus: () => {
    set({
      selectedMission: null,
      error: null,
      errorCode: null,
    });
  },
}));

export default useMissionStore;
