import axiosInstance from '../axios';

// Types
export interface Statistics {
  label: string;
  amount: number;
  changeRate: number;
  isPositive: boolean;
}

export interface StatisticsData {
  deposit: Statistics;
  investment: Statistics;
  loan: Statistics;
}

export interface Student {
  id: number;
  name: string;
  cash: number;
  investment: number;
  loan: number;
  creditGrade: string;
  missionStatus: '검토 필요' | '수행 중' | '수행 완료';
  loanStatus: '검토 필요' | '승인' | '거절';
  items: number;
}

export interface StudentDashboardData {
  cash: number;
  investment: number;
  loan: number;
  totalAssets: number;
  creditGrade: string;
}

export interface DashboardResponse {
  isSuccess: boolean;
  data: {
    statistics: StatisticsData;
    students: Student[];
  };
  code?: string;
  message?: string;
}

export interface StudentDashboardResponse {
  isSuccess: boolean;
  data: StudentDashboardData;
  code?: string;
  message?: string;
}

export const DashboardService = {
  /**
   * 교사 대시보드 데이터 조회
   */
  getTeacherDashboard: async () => {
    try {
      const response = await axiosInstance.get<DashboardResponse>('/dashboards/my-class');
      return response.data;
    } catch (error) {
      console.error('Failed to fetch dashboard data:', error);
      throw error;
    }
  },

  /**
   * 학생 대시보드 데이터 조회
   */
  getStudentDashboard: async () => {
    try {
      const response = await axiosInstance.get<StudentDashboardResponse>('/dashboards/my');
      return response.data;
    } catch (error) {
      console.error('Failed to fetch student dashboard data:', error);
      throw error;
    }
  },

  /**
   * 학생 미션 검토하기
   */
  reviewStudentMission: async (studentId: number) => {
    try {
      const response = await axiosInstance.get(`/missions/student/${studentId}/review`);
      return response.data;
    } catch (error) {
      console.error('Failed to review student mission:', error);
      throw error;
    }
  },

  /**
   * 학생 대출 검토하기
   */
  reviewStudentLoan: async (studentId: number) => {
    try {
      const response = await axiosInstance.get(`/loans/student/${studentId}/review`);
      return response.data;
    } catch (error) {
      console.error('Failed to review student loan:', error);
      throw error;
    }
  }
};

