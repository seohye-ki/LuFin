export type LoanProductDTO = {
  loanProductId: number;
  name: string;
  maxAmount: number;
  interestRate: number;
  period: number;
};

export type LoanProductListDTO = LoanProductDTO[];

export type LoanApplicationDTO = {
  loanApplicationId: number;
  memberId: number;
  memberName: string;
  loanProductId: number;
  productName: string;
  requiredAmount: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'OPEN' | 'OVERDUED' | 'CLOSED';
  startedAt: string;
  dueDate: string;
};

export type LoanApplicationListDTO = LoanApplicationDTO[];

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
  nextPaymentDate: string;
  createdAt: string;
  dueDate: string;
};

export type ApplyLoanDTO = {
  loanProductId: number;
  requestedAmount: number;
  description: string;
};
