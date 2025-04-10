import axiosInstance from '../axios';
import {
  MissionRaw,
  MissionList,
  MissionCreateRequest,
  MissionUpdateRequest,
  ParticipationUserInfo,
} from '../../../types/mission/mission';
import { AxiosError } from 'axios';

export interface MissionResponse<T> {
  isSuccess: boolean;
  data?: T;
  code?: string;
  message?: string;
}

const MISSION_ENDPOINT = '/missions';

export const missionService = {
  /**
   * 미션 목록 조회
   */
  getMissionList: async () => {
    try {
      console.log('API 요청 시작: 미션 목록 조회');

      const response =
        await axiosInstance.get<MissionResponse<{ myData: MissionList[]; allData: MissionList[] }>>(
          MISSION_ENDPOINT,
        );
      console.log('API 응답:', response.data);

      if (response.data.isSuccess && response.data.data) {
        const { myData, allData } = response.data.data;
        return {
          success: true,
          myMissions: myData,
          allMissions: allData,
        };
      } else {
        console.log('API 응답 실패:', response.data.message);
        return {
          success: false,
          code: response.data.code,
          message: response.data.message || '미션 목록을 불러오는데 실패했습니다.',
        };
      }
    } catch (error) {
      console.error('미션 목록 조회 오류:', error);
      const message = '미션 목록을 불러오는데 실패했습니다.';
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
    try {
      console.log('API 요청 시작: 미션 상세 조회');

      const response = await axiosInstance.get<MissionResponse<MissionRaw>>(
        `${MISSION_ENDPOINT}/${missionId}`,
      );

      console.log('API 응답:', response.data);
      if (response.data.isSuccess && response.data.data) {
        return {
          success: true,
          mission: response.data.data,
        };
      } else {
        console.log('API 응답 실패:', response.data.message);
        return {
          success: false,
          code: response.data.code,
          message: response.data.message || '미션 상세를 불러오는데 실패했습니다.',
        };
      }
    } catch (error) {
      console.error('미션 상세 조회 오류:', error);
      const message = '미션 상세를 불러오는데 실패했습니다.';
      return {
        success: false,
        message,
      };
    }
  },

  /**
   * 미션 생성
   */
  createMission: async (mission: MissionCreateRequest) => {
    try {
      const response = await axiosInstance.post<MissionResponse<{ missionId: number }>>(
        MISSION_ENDPOINT,
        mission,
      );

      console.log('API 응답:', response.data);

      if (response.data.isSuccess && response.data.data) {
        return {
          success: true,
          missionId: response.data.data.missionId,
        };
      } else {
        console.log('API 응답 실패:', response.data.message);
        return {
          success: false,
          code: response.data.code,
          message: response.data.message || '미션 생성에 실패했습니다.',
        };
      }
    } catch (error) {
      console.error('미션 생성 오류:', error);
      const message = '미션 생성에 실패했습니다.';
      return {
        success: false,
        message,
      };
    }
  },

  /**
   * 미션 수정
   */
  updateMission: async (mission: MissionUpdateRequest) => {
    try {
      const { missionId, ...data } = mission;
      const response = await axiosInstance.put<MissionResponse<{ missionId: number }>>(
        `${MISSION_ENDPOINT}/${missionId}`,
        data,
      );

      console.log('API 응답:', response.data);

      if (response.data.isSuccess && response.data.data) {
        return {
          success: true,
        };
      } else {
        console.log('API 응답 실패:', response.data.message);
        return {
          success: false,
          code: response.data.code,
          message: response.data.message || '미션 수정에 실패했습니다.',
        };
      }
    } catch (error) {
      console.error('미션 수정 오류:', error);
      const message = '미션 수정에 실패했습니다.';
      return {
        success: false,
        message,
      };
    }
  },
  /**
   * 미션 삭제
   */
  deleteMission: async (missionId: number) => {
    try {
      const response = await axiosInstance.delete(`/missions/${missionId}`, {
        headers: {
          'Content-Type': 'application/json',
        },
        data: undefined,
      });

      if (response.status === 204) {
        return { success: true };
      }

      const message = response.data?.message || '미션 삭제에 실패했습니다.';
      return {
        success: false,
        message,
        code: response.data?.code,
      };
    } catch (error: unknown) {
      console.error('미션 삭제 오류:', error);

      const axiosError = error as AxiosError<{ message: string; code: string }>;
      const message = axiosError.response?.data?.message || '알 수 없는 오류가 발생했습니다.';
      return {
        success: false,
        message,
        code: axiosError.response?.data?.code,
      };
    }
  },
  /**
   * 미션 참여자 목록 조회
   */
  getParticipationList: async (missionId: number) => {
    try {
      const response = await axiosInstance.get<MissionResponse<ParticipationUserInfo[]>>(
        `${MISSION_ENDPOINT}/${missionId}/participations`,
      );
      if (response.data.isSuccess && response.data.data) {
        return { success: true, participations: response.data.data };
      } else {
        return { success: false, message: response.data.message };
      }
    } catch (error) {
      console.error('참여자 조회 오류:', error);
      return { success: false, message: '참여자 목록 조회에 실패했습니다.' };
    }
  },
  /**
   * 미션 신청
   */
  applyMission: async (missionId: number) => {
    try {
      const response = await axiosInstance.post<MissionResponse<{ participationId: number }>>(
        `${MISSION_ENDPOINT}/${missionId}/participations`,
        null,
      );

      if (response.data.isSuccess && response.data.data) {
        return { success: true, participationId: response.data.data.participationId };
      } else {
        return { success: false, message: response.data.message };
      }
    } catch (error) {
      console.error('미션 신청 오류:', error);
      return { success: false, message: '미션 신청에 실패했습니다.' };
    }
  },
  /**
   * 미션 리뷰 요청
   */
  requestReview: async (missionId: number, participationId: number) => {
    try {
      const response = await axiosInstance.patch<MissionResponse<{ participationId: number }>>(
        `${MISSION_ENDPOINT}/${missionId}/participations/${participationId}`,
      );
      if (response.data.isSuccess) {
        return { success: true };
      } else {
        return { success: false, message: response.data.message };
      }
    } catch (error) {
      console.error('리뷰 요청 실패:', error);
      return { success: false, message: '리뷰 요청에 실패했습니다.' };
    }
  },
  /**
   * 미션 상태 변경
   */
  changeMissionStatus: async (
    participationId: number,
    status: 'SUCCESS' | 'FAILED' | 'REJECTED' | 'CHECKING',
  ) => {
    try {
      const response = await axiosInstance.patch<
        MissionResponse<{ participationId: number; status: string }>
      >(`${MISSION_ENDPOINT}/participations/${participationId}`, { status });
      if (response.data.isSuccess) {
        return { success: true, participationId: response.data.data?.participationId };
      } else {
        return { success: false, message: response.data.message, code: response.data.code };
      }
    } catch (error) {
      console.error('미션 상태 변경 오류:', error);
      return { success: false, message: '미션 상태 변경에 실패했습니다.' };
    }
  },
};
