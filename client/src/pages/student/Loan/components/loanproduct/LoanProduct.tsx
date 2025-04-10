import React from 'react';
import Penguin from '../../../../../assets/svgs/penguin.svg';
import Cat from '../../../../../assets/svgs/cat.svg';
import Tiger from '../../../../../assets/svgs/tiger.svg';
import { LoanProductDTO } from '../../../../../types/Loan/loan';
import Lufin from '../../../../../components/Lufin/Lufin';

const LOAN_TYPES = {
  '펭귄 대출': {
    icon: Penguin,
    bg: 'bg-success-60',
    color: 'text-success',
  },
  '고양이 대출': {
    icon: Cat,
    bg: 'bg-yellow',
    color: 'text-warning',
  },
  '호랑이 대출': {
    icon: Tiger,
    bg: 'bg-danger-60',
    color: 'text-danger',
  },
} as const;

interface LoanProductCardProps {
  loanProduct: LoanProductDTO;
  onClick: (loanProduct: LoanProductDTO) => void;
}

const LoanProduct: React.FC<LoanProductCardProps> = ({ loanProduct, onClick }) => {
  const loan = LOAN_TYPES[loanProduct.name as keyof typeof LOAN_TYPES];

  return (
    loan && (
      <div
        className={`w-full h-fit p-4 rounded-2xl flex flex-col gap-3 ${loan.bg} cursor-pointer hover:opacity-80 transition`}
        onClick={() => onClick(loanProduct)}
      >
        <div className='w-full h-fit flex flex-row justify-between items-start'>
          <img src={loan.icon} />
          <div className='w-fit h-fit flex flex-row gap-1 items-center'>
            <div className='w-12 h-6 bg-white rounded-full flex justify-center items-center'>
              <p className={`font-semibold text-c2 ${loan.color}`}>
                월 {Math.floor(loanProduct.interestRate * 100)}%
              </p>
            </div>
            <div className='w-12 h-6 bg-white rounded-full flex justify-center items-center'>
              <p className={`font-semibold text-c2 ${loan.color}`}>{loanProduct.period}일</p>
            </div>
          </div>
        </div>
        <Lufin size='l' count={loanProduct.maxAmount} />
        <p className='font-medium text-c1 text-black'>{loanProduct.name}</p>
      </div>
    )
  );
};

export default LoanProduct;
