import { dateUtil, DateUtil } from '../../libs/utils/date-util';
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
}[];

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

export const sampleLoanApplications: LoanApplicationListDTO = [
  {
    loanApplicationId: 101,
    memberId: 1001,
    memberName: '김철수',
    loanProductId: 1,
    loanProductName: '펭귄 대출',
    requiredAmount: 300000,
    status: 'PENDING',
    createdAt: dateUtil('2025-01-01T09:30:30'),
    dueDate: dateUtil('2025-12-31T23:59:59'),
  },
  {
    loanApplicationId: 102,
    memberId: 1001,
    memberName: '김철수',
    loanProductId: 2,
    loanProductName: '고양이 대출',
    requiredAmount: 800000,
    status: 'APPROVED',
    createdAt: dateUtil('2025-02-15T14:10:10'),
    dueDate: dateUtil('2026-06-30T23:59:59'),
  },
  {
    loanApplicationId: 103,
    memberId: 1001,
    memberName: '김철수',
    loanProductId: 3,
    loanProductName: '호랑이 대출',
    requiredAmount: 1500000,
    status: 'REJECTED',
    createdAt: dateUtil('2025-03-10T11:45:00'),
    dueDate: dateUtil('2026-09-30T23:59:59'),
  },
  {
    loanApplicationId: 103,
    memberId: 1001,
    memberName: '김철수',
    loanProductId: 3,
    loanProductName: '호랑이 대출',
    requiredAmount: 1500000,
    status: 'REJECTED',
    createdAt: dateUtil('2025-03-10T11:45:00'),
    dueDate: dateUtil('2026-09-30T23:59:59'),
  },
  {
    loanApplicationId: 103,
    memberId: 1001,
    memberName: '김철수',
    loanProductId: 3,
    loanProductName: '호랑이 대출',
    requiredAmount: 1500000,
    status: 'REJECTED',
    createdAt: dateUtil('2025-03-10T11:45:00'),
    dueDate: dateUtil('2026-09-30T23:59:59'),
  },
  {
    loanApplicationId: 103,
    memberId: 1001,
    memberName: '김철수',
    loanProductId: 3,
    loanProductName: '호랑이 대출',
    requiredAmount: 1500000,
    status: 'REJECTED',
    createdAt: dateUtil('2025-03-10T11:45:00'),
    dueDate: dateUtil('2026-09-30T23:59:59'),
  },
];

export const sampleLoanApplicationDetail: LoanApplicationDetailDTO = {
  loanApplicationId: 101,
  memberId: 1001,
  memberName: '김철수',
  loanProductId: 1,
  loanProductName: '펭귄 대출',
  interestRate: 4,
  requiredAmount: 300000,
  description: '생활비 부족으로 신청',
  status: 'PENDING',
  interestAmount: 12000,
  overdueCount: 0,
  nextPaymentDate: dateUtil('2025-05-01T00:00:00'),
  createdAt: dateUtil('2025-01-01T09:30:30'),
  dueDate: dateUtil('2025-12-31T23:59:59'),
};
