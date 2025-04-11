import React from 'react';
import Card from '../../../../../components/Card/Card';
import Lufin from '../../../../../components/Lufin/Lufin';
import { LoanApplicationDetailDTO } from '../../../../../types/Loan/loan';
import { dateUtil } from '../../../../../libs/utils/date-util';

interface loanAPplicationCardsProps {
  loanApplicationDetail: LoanApplicationDetailDTO | null;
}

const LoanApplicationCards: React.FC<loanAPplicationCardsProps> = ({ loanApplicationDetail }) => {
  return loanApplicationDetail ? (
    <div className='flex flex-row gap-4'>
      <Card titleLeft='나의 대출 금액' titleSize='s' className='w-full'>
        <Lufin size='l' count={loanApplicationDetail.requiredAmount} />
      </Card>
      <Card titleLeft='다음 이자 납부일' titleSize='s' className='w-full'>
        <p className='text-black text-h2 font-semibold'>
          {dateUtil(loanApplicationDetail.nextPaymentDate).formattedDate}
        </p>
      </Card>
      <Card titleLeft='이번주에 낼 이자' titleSize='s' className='w-full'>
        <Lufin size='l' count={loanApplicationDetail.interestAmount} />
      </Card>
      <Card titleLeft='만기까지' titleSize='s' className='w-full'>
        <p className='text-black text-h2 font-semibold'>
          {dateUtil(loanApplicationDetail.dueDate).remainingDays}{' '}
          <span className='text-p1 font-regular'>일</span>
        </p>
      </Card>
    </div>
  ) : (
    <p>아직 대출 받은 정보가 없어요.</p>
  );
};

export default LoanApplicationCards;
