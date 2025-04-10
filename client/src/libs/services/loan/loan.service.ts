import {
  ApplyLoanDTO,
  LoanApplicationDetailDTO,
  LoanApplicationDTO,
  LoanProductDTO,
} from '../../../types/Loan/loan';
import axiosInstance from '../axios';

export const getLoanProductList = async (): Promise<LoanProductDTO[]> => {
  try {
    const response = await axiosInstance.get<{ data: LoanProductDTO[] }>('/loans/products');
    return response.data.data;
  } catch (error) {
    console.error('Error fetching loan products:', error);
    throw error;
  }
};

export const getLoanApplicationList = async (): Promise<LoanApplicationDTO[]> => {
  try {
    const response = await axiosInstance.get<{ data: LoanApplicationDTO[] }>('/loans/applications');
    ('');
    return response.data.data;
  } catch (error) {
    console.error('Error fetching loan aplications:', error);
    throw error;
  }
};

export const applyLoan = async (applyLoanDTO: ApplyLoanDTO): Promise<void> => {
  try {
    ('');
    await axiosInstance.post('/loans/applications', applyLoanDTO);
  } catch (error) {
    console.error('Error post loan aplication:', error);
    throw error;
  }
};

export const getLoanApplicationDetail = async (
  loanApplicationId: number,
): Promise<LoanApplicationDetailDTO> => {
  try {
    const response = await axiosInstance.get(`/loans/applications/${loanApplicationId}`);
    return response.data.data;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const approveLoanApplication = async (
  loanApplicationId: number,
  isApproved: boolean,
): Promise<boolean> => {
  const response = await axiosInstance.patch(`/loans/applications/${loanApplicationId}`, {
    status: isApproved ? 'APPROVED' : 'REJECTED',
  });
  return response.data.isSuccess;
};
