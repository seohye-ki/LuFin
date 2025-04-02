import { DateUtil } from '../../libs/utils/date-util';

export type LoanProductDTO = {
  loanProductId: number;
  name: string;
  maxAmount: number;
  interestRate: DoubleRange;
  period: number;
};

export type LoanApplicationListDTO = {
  loanApplicationId: number;
  memberId: number;
  memberName: string;
  loanProductId: number;
  loanProductName: string;
  requiredAmount: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'OPEN' | 'OVERDUED' | 'CLOSED';
  createdAt: DateUtil;
  dueDate: DateUtil;
};

export type LoanApplicationDetailDTO = {
  loanApplicationId: number;
  memberId: number;
  memberName: string;
  loanProductId: number;
  loanProductName: string;
  interestRate: number;
  requiredAmount: number;
  description: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'OPEN' | 'OVERDUED' | 'CLOSED';
  interestAmount: number;
  overdueCount: number;
  nextPaymentDate: DateUtil;
  createdAt: DateUtil;
  dueDate: DateUtil;
};
