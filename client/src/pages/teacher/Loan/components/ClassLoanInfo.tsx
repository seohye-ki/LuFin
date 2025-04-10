import React from 'react';
import Card from '../../../../components/Card/Card';
import { LoanApplicationDTO } from '../../../../types/Loan/loan';
import Lufin from '../../../../components/Lufin/Lufin';

interface ClassLoanInfoProps {
  loanApplicationList: LoanApplicationDTO[];
}

const ClassLoanInfo: React.FC<ClassLoanInfoProps> = ({ loanApplicationList }) => {
  // 진행 중인 대출만 필터링
  const openLoans = loanApplicationList.filter((item) => item.status === 'OPEN');

  const totalAmount = openLoans.reduce((acc, loan) => acc + loan.requiredAmount, 0);
  const totalCount = openLoans.length;
  const repaidCount = loanApplicationList.filter((item) => item.status === 'CLOSED').length;
  const overdueCount = loanApplicationList.filter((item) => item.status === 'OVERDUED').length;

  return (
    <div className='w-full h-fit flex flex-row gap-4'>
      <Card titleLeft='우리반 부채' titleSize='s' className='w-full'>
        <Lufin count={totalAmount} size='l' />
      </Card>
      <Card titleLeft='대출 실행중' titleSize='s' className='w-full'>
        <p className='text-black text-h2 font-semibold'>
          {totalCount}
          <span className='text-p1 text-black'>명</span>
        </p>
      </Card>
      <Card titleLeft='상환 완료' titleSize='s' className='w-full'>
        <p className='text-black text-h2 font-semibold'>
          {repaidCount}
          <span className='text-p1 text-black'>명</span>
        </p>
      </Card>
      <Card titleLeft='이자 연체중' titleSize='s' className='w-full'>
        <p className='text-black text-h2 font-semibold'>
          {overdueCount}
          <span className='text-p1 text-black'>명</span>
        </p>
      </Card>
    </div>
  );
};

export default ClassLoanInfo;
