import { axiosInstance } from '../axios';

interface CreditRecoveryResponse {
  isSuccess: boolean;
  data: {
    memberId: number;
    score: number;
    grade: string;
    creditStatus: number;
    creditStatusDescription: string;
    createdAt: string;
    updatedAt: string;
  };
}

export const CreditService = {
  approveRecovery: async (memberId: number): Promise<CreditRecoveryResponse> => {
    const response = await axiosInstance.patch<CreditRecoveryResponse>(`/credit/recovery/${memberId}`);
    return response.data;
  }
}; 