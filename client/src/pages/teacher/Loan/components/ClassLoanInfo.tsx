import React from 'react';
import Card from '../../../../components/Card/Card';
import { LoanApplicationDTO } from '../../../../types/Loan/loan';

interface ClassLoanInfoProps {
  loanApplicationList: LoanApplicationDTO[];
}

const ClassLoanInfo: React.FC<ClassLoanInfoProps> = ({ loanApplicationList }) => {
  const totalAmount = loanApplicationList.reduce(
    (acc, loanApplication) => acc + loanApplication.requiredAmount,
    0,
  );
  const totalCount = loanApplicationList.length;
  const repaidCount = loanApplicationList.filter((item) => item.status === 'CLOSED').length;
  const overdueCount = loanApplicationList.filter((item) => item.status === 'OVERDUED').length;

  return (
    <div className='w-full h-fit flex flex-row gap-4'>
      <Card titleLeft='우리반 부채' titleSize='s' className='w-full'>
        <p className='text-black text-h2 font-semibold'> {totalAmount} </p>
      </Card>
      <Card titleLeft='총 대출 횟수' titleSize='s' className='w-full'>
        <p className='text-black text-h2 font-semibold'> {totalCount} </p>
      </Card>
      <Card titleLeft='상환 완료' titleSize='s' className='w-full'>
        <p className='text-black text-h2 font-semibold'>{repaidCount}</p>
      </Card>
      <Card titleLeft='이자 연체중' titleSize='s' className='w-full'>
        <p className='text-black text-h2 font-semibold'>{overdueCount}</p>
      </Card>
    </div>
  );
};

export default ClassLoanInfo;
