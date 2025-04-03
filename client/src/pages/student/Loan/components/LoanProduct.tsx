import React from 'react';
import Penguin from '../../../../assets/svgs/Penguin.svg';
import Cat from '../../../../assets/svgs/Cat.svg';
import Tiger from '../../../../assets/svgs/Tiger.svg';
import { LoanProductDTO } from '../../../../types/Loan/loan';
import Lufin from '../../../../components/Lufin/Lufin';

const LOAN_TYPES = {
  PENGUIN: {
    icon: Penguin,
    bg: 'bg-success-60',
    color: 'text-success',
  },
  CAT: {
    icon: Cat,
    bg: 'bg-yellow',
    color: 'text-warning',
  },
  TIGER: {
    icon: Tiger,
    bg: 'bg-danger-60',
    color: 'text-danger',
  },
} as const;

type LoanType = keyof typeof LOAN_TYPES;

export type LoanProductProps = LoanProductDTO & {
  type: LoanType;
  onClick?: () => void; // 클릭 이벤트 추가
};

const LoanProduct: React.FC<LoanProductProps> = ({
  type,
  name,
  period,
  interestRate,
  maxAmount,
  onClick, // 부모 컴포넌트에서 받을 클릭 이벤트
}) => {
  const loan = LOAN_TYPES[type];

  return (
    <div
      className={`w-full h-fit p-4 rounded-2xl flex flex-col gap-3 ${loan.bg} cursor-pointer hover:opacity-80 transition`}
      onClick={onClick} // 클릭 이벤트 연결
    >
      <div className='w-full h-fit flex flex-row justify-between items-start'>
        <img src={loan.icon} alt={type} />
        <div className='w-fit h-fit flex flex-row gap-1 items-center'>
          <div className='w-12 h-6 bg-white rounded-full flex justify-center items-center'>
            <p className={`font-semibold text-c2 ${loan.color}`}>월 {interestRate}%</p>
          </div>
          <div className='w-12 h-6 bg-white rounded-full flex justify-center items-center'>
            <p className={`font-semibold text-c2 ${loan.color}`}>{period}일</p>
          </div>
        </div>
      </div>
      <Lufin size='l' count={maxAmount} />
      <p className='font-medium text-c1 text-black'>{name}</p>
    </div>
  );
};

export default LoanProduct;
