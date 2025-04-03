import React from 'react';
import Card from '../../../../components/Card/Card';
import Button from '../../../../components/Button/Button';
import Badge from '../../../../components/Badge/Badge';
import { LoanApplicationDetailDTO, sampleLoanApplicationDetail } from '../../../../types/Loan/loan';

// 상태 매핑 객체
const statusMapping: Record<
  'PENDING' | 'APPROVED' | 'REJECTED' | 'OPEN' | 'OVERDUED' | 'CLOSED',
  { badgeStatus: 'ready' | 'ing' | 'reject' | 'fail' | 'done'; label: string }
> = {
  PENDING: { badgeStatus: 'ready', label: '신청' },
  APPROVED: { badgeStatus: 'done', label: '승인' },
  REJECTED: { badgeStatus: 'reject', label: '거절' },
  OPEN: { badgeStatus: 'ing', label: '진행중' },
  OVERDUED: { badgeStatus: 'fail', label: '연체' },
  CLOSED: { badgeStatus: 'done', label: '종료' },
};

interface DetailLoanApplicationProps {
  loanId: number;
  closeModal: () => void;
}

const DetailLoanApplication: React.FC<DetailLoanApplicationProps> = ({ loanId, closeModal }) => {
  console.log(loanId);
  const [loanDetail] = React.useState<LoanApplicationDetailDTO>(sampleLoanApplicationDetail);

  const { badgeStatus, label } = statusMapping[loanDetail.status] || {
    badgeStatus: 'ready',
    label: '알 수 없음',
  };

  return (
    <div
      className='fixed top-0 left-0 right-0 bottom-0 flex justify-center items-center bg-black/80 z-50'
      onClick={closeModal}
    >
      <div onClick={(e) => e.stopPropagation()}>
        <Card
          className='w-104 h-fit'
          titleLeft='대출 상세 정보'
          titleRight={<Badge status={badgeStatus}>{label}</Badge>}
        >
          <div className='flex flex-col gap-6'>
            <DetailSection label='신청자'>{loanDetail.memberName}</DetailSection>
            <DetailSection label='상품명'>{loanDetail.loanProductName}</DetailSection>
            <DetailSection label='대출 금액'>
              {loanDetail.requiredAmount.toLocaleString()} 원
            </DetailSection>
            <DetailSection label='이자율'>{loanDetail.interestRate}%</DetailSection>
            <DetailSection label='만기일'>{loanDetail.dueDate.formattedDate}</DetailSection>

            <DetailSection label='다음 납부일'>{loanDetail.dueDate.formattedDate}</DetailSection>
          </div>
          <Button onClick={closeModal}>확인</Button>
        </Card>
      </div>
    </div>
  );
};

const DetailSection: React.FC<{ label: string; children: React.ReactNode }> = ({
  label,
  children,
}) => {
  return (
    <div className='flex flex-col gap-2'>
      <p className='text-p2 font-semibold text-grey'>{label}</p>
      <p className='text-p1'>{children}</p>
    </div>
  );
};

export default DetailLoanApplication;
