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
  myMissions: MissionList[];
  availableMissions: MissionList[];
  selectedMission: MissionRaw | null;
  participationId: number | null;
  setParticipationId: (participationId: number) => void;
  isLoading: boolean;
  error: string | null;
  errorCode: string | null;

  getMissionList: () => Promise<{ success: boolean; message?: string; code?: string }>;
  getMissionDetail: (
    missionId: number,
  ) => Promise<{ success: boolean; message?: string; code?: string; mission?: MissionRaw }>;
  createMission: (
    mission: MissionCreateRequest,
  ) => Promise<{ success: boolean; missionId?: number; message?: string }>;
  updateMission: (mission: MissionUpdateRequest) => Promise<{ success: boolean; message?: string }>;
  applyMission: (missionId: number) => Promise<{
    success: boolean;
    participationId?: number;
    message?: string;
  }>;
  deleteMission: (missionId: number) => Promise<{ success: boolean; message?: string }>;
  requestReview: (
    missionId: number,
    participationId: number,
  ) => Promise<{ success: boolean; message?: string }>;
  getMissionWithParticipants: (
    missionId: number,
  ) => Promise<{ success: boolean; message?: string }>;
  getParticipationList: (
    missionId: number,
  ) => Promise<{ success: boolean; message?: string; participations?: ParticipationUserInfo[] }>;
  changeMissionStatus: (
    participationId: number,
    status: 'SUCCESS' | 'FAILED' | 'REJECTED' | 'CHECKING',
  ) => Promise<{ success: boolean; message?: string }>;
  getMissionStatus: () => {
    myMissions: MissionList[];
    availableMissions: MissionList[];
    selectedMission: MissionRaw | null;
    isLoading: boolean;
    error: string | null;
    errorCode: string | null;
  };
  resetMissionStatus: () => void;
}

const useMissionStore = create<MissionState>((set, get) => ({
  myMissions: [],
  availableMissions: [],
  selectedMission: null,
  participationId: null,
  isLoading: false,
  error: null,
  errorCode: null,

  setParticipationId: (participationId: number) => set({ participationId }),

  getMissionList: async () => {
    set({ isLoading: true, error: null, errorCode: null });
    try {
      const result = await missionService.getMissionList();
      if (result.success) {
        const { myMissions, allMissions } = result;
        const availableMissions = allMissions?.filter(
          (m) => !myMissions?.find((my) => my.missionId === m.missionId),
        );
        set({ myMissions, availableMissions, isLoading: false });
      } else {
        set({ isLoading: false, error: result.message || null, errorCode: result.code || null });
      }
      return result;
    } catch (error) {
      console.error(error);
      set({ isLoading: false, error: '미션 목록을 불러오는데 실패했습니다.', errorCode: null });
      return { success: false, message: '미션 목록을 불러오는데 실패했습니다.' };
    }
  },

  getMissionDetail: async (missionId: number) => {
    set({ isLoading: true, error: null, errorCode: null });
    try {
      const result = await missionService.getMissionDetail(missionId);
      if (result.success) {
        set({ selectedMission: result.mission, isLoading: false });
        return {
          success: true,
          mission: result.mission,
        };
      } else {
        set({ isLoading: false, error: result.message || null, errorCode: result.code || null });
        return { success: false, message: result.message, code: result.code };
      }
    } catch (error) {
      console.error(error);
      set({ isLoading: false, error: '미션 상세를 불러오는데 실패했습니다.', errorCode: null });
      return { success: false, message: '미션 상세를 불러오는데 실패했습니다.' };
    }
  },

  getMissionWithParticipants: async (missionId: number) => {
    set({ isLoading: true });
    const [missionRes, participationRes] = await Promise.all([
      missionService.getMissionDetail(missionId),
      missionService.getParticipationList(missionId),
    ]);
    if (missionRes.success && participationRes.success) {
      set({ selectedMission: missionRes.mission, isLoading: false });
      return { success: true };
    } else {
      set({ isLoading: false });
      return { success: false, message: '미션 상세 조회에 실패했습니다.' };
    }
  },

  createMission: async (mission) => {
    set({ isLoading: true, error: null, errorCode: null });
    try {
      const result = await missionService.createMission(mission);
      if (result.success) {
        set({ isLoading: false });
        return { success: true, missionId: result.missionId };
      } else {
        set({ isLoading: false, error: result.message || null });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error(error);
      set({ isLoading: false, error: '미션 생성에 실패했습니다.' });
      return { success: false, message: '미션 생성에 실패했습니다.' };
    }
  },

  updateMission: async (mission) => {
    set({ isLoading: true, error: null, errorCode: null });
    try {
      const result = await missionService.updateMission(mission);
      if (result.success) {
        set({ isLoading: false });
        return { success: true, message: result.message };
      } else {
        set({ isLoading: false, error: result.message || null });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error(error);
      set({ isLoading: false, error: '미션 수정에 실패했습니다.' });
      return { success: false, message: '미션 수정에 실패했습니다.' };
    }
  },

  deleteMission: async (missionId) => {
    set({ isLoading: true });
    try {
      const result = await missionService.deleteMission(missionId);
      if (result.success) {
        await get().getMissionList();
        set({ isLoading: false });
        return { success: true };
      } else {
        set({ isLoading: false, error: result.message || null, errorCode: result.code || null });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error(error);
      set({ isLoading: false, error: '미션 삭제에 실패했습니다.' });
      return { success: false, message: '미션 삭제에 실패했습니다.' };
    }
  },

  getParticipationList: async (missionId) => {
    return await missionService.getParticipationList(missionId);
  },

  applyMission: async (missionId) => {
    const result = await missionService.applyMission(missionId);

    if (result.success && result.participationId !== undefined) {
      set({ participationId: result.participationId });
    }

    return result;
  },

  requestReview: async (missionId, participationId) => {
    return await missionService.requestReview(missionId, participationId);
  },

  changeMissionStatus: async (participationId, status) => {
    return await missionService.changeMissionStatus(participationId, status);
  },

  getMissionStatus: () => {
    const state = get();
    return {
      myMissions: state.myMissions,
      availableMissions: state.availableMissions,
      selectedMission: state.selectedMission,
      isLoading: state.isLoading,
      error: state.error,
      errorCode: state.errorCode,
    };
  },

  resetMissionStatus: () => {
    set({ selectedMission: null, error: null, errorCode: null });
  },
}));

export default useMissionStore;
