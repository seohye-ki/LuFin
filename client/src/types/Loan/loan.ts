import { DateUtil } from '../../libs/utils/date-util';
import { LoanProductProps } from '../../pages/student/Loan/components/LoanProduct';

export type LoanProductDTO = {
  loanProductId: number;
  name: string;
  maxAmount: number;
  interestRate: number;
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

export const sampleLoanProducts: LoanProductProps[] = [
  {
    type: 'PENGUIN',
    loanProductId: 1,
    name: '펭귄 대출',
    period: 30,
    interestRate: 4,
    maxAmount: 500000,
  },
  {
    type: 'CAT',
    loanProductId: 2,
    name: '고양이 대출',
    period: 60,
    interestRate: 8,
    maxAmount: 1000000,
  },
  {
    type: 'TIGER',
    loanProductId: 3,
    name: '호랑이 대출',
    period: 90,
    interestRate: 12,
    maxAmount: 2000000,
  },
];
