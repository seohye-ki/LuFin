import React, { useEffect, useState } from 'react';
import Card from '../../../../../components/Card/Card';
import Button from '../../../../../components/Button/Button';
import Badge from '../../../../../components/Badge/Badge';
import { LoanApplicationDetailDTO, LoanApplicationDTO } from '../../../../../types/Loan/loan';
import {
  approveLoanApplication,
  getLoanApplicationDetail,
} from '../../../../../libs/services/loan/loan.service';
import { dateUtil } from '../../../../../libs/utils/date-util';
import useAuthStore from '../../../../../libs/store/authStore';
import { hideGlobalAlert, showGlobalAlert } from '../../../../../libs/store/alertStore';

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
  loanApplication: LoanApplicationDTO;
  closeModal: () => void;
}

const DetailLoanApplication: React.FC<DetailLoanApplicationProps> = ({
  loanApplication,
  closeModal,
}) => {
  const [loanApplicationDetail, setLoanApplicationDetail] =
    useState<LoanApplicationDetailDTO | null>(null);

  const { badgeStatus, label } = statusMapping[loanApplication.status] || {
    badgeStatus: 'ready',
    label: '알 수 없음',
  };

  const { userRole } = useAuthStore();

  const fetchApproveLoanApplication = async (loanApplicationId: number, isApproved: boolean) => {
    showGlobalAlert(
      isApproved ? '대출을 승인하시겠습니까?' : '대출을 거절하시겠습니까?',
      null,
      isApproved ? '신청한 대출 금액은 학생에게 지급됩니다.' : '거절 후 다시 신청할 수 있습니다.',
      'info',
      {
        label: '확인',
        onClick: async () => {
          const res = await approveLoanApplication(loanApplicationId, isApproved);

          if (res) {
            showGlobalAlert(
              isApproved ? '대출 승인이 완료됐습니다.' : '대출 거절이 완료됐습니다.',
              null,
              '',
              'info',
              {
                label: '확인',
                onClick: () => {
                  closeModal();
                  hideGlobalAlert();
                },
              },
            );
          }
        },
      },
      {
        label: '취소',
        onClick: () => {
          hideGlobalAlert();
        },
      },
    );
  };

  useEffect(() => {
    const fetchDetail = async () => {
      try {
        const detail = await getLoanApplicationDetail(loanApplication.loanApplicationId);
        ('');
        setLoanApplicationDetail(detail);
      } catch (error) {
        console.error('대출 신청 상세 정보를 불러오는 데 실패했습니다.', error);
      }
    };

    fetchDetail();
  }, [loanApplication.loanApplicationId]);

  if (!loanApplicationDetail) {
    return (
      <div
        className='fixed top-0 left-0 right-0 bottom-0 flex justify-center items-center bg-black/80 z-50'
        onClick={closeModal}
      >
        <div onClick={(e) => e.stopPropagation()}>
          <Card className='w-104 h-fit' titleLeft='대출 상세 정보'>
            <p className='text-center p-4'>불러오는 중...</p>
          </Card>
        </div>
      </div>
    );
  }

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
            <DetailSection label='신청자'>{loanApplication.memberName}</DetailSection>
            <DetailSection label='상품명'>{loanApplication.productName}</DetailSection>
            <DetailSection label='신청 금액'>
              {loanApplication.requiredAmount.toLocaleString()} 원
            </DetailSection>
            <DetailSection label='이자율'>
              {Math.floor(loanApplicationDetail.interestRate * 100)}%
            </DetailSection>
            {loanApplicationDetail.status === 'OPEN' && (
              <DetailSection label='만기일'>
                {dateUtil(loanApplicationDetail.dueDate).formattedDate}
              </DetailSection>
            )}
            {loanApplicationDetail.status === 'OPEN' && (
              <DetailSection label='다음 납부일'>
                {dateUtil(loanApplicationDetail.nextPaymentDate).formattedDate}
              </DetailSection>
            )}
            <DetailSection label='사유'>{loanApplicationDetail.description}</DetailSection>
          </div>
          {userRole === 'TEACHER' && loanApplication.status === 'PENDING' ? (
            <div className='flex justify-center gap-4 mt-6'>
              <Button
                className='w-full'
                color='danger'
                onClick={() =>
                  fetchApproveLoanApplication(loanApplicationDetail.loanApplicationId, false)
                }
              >
                거절하기
              </Button>
              <Button
                className='w-full'
                color='primary'
                onClick={() =>
                  fetchApproveLoanApplication(loanApplicationDetail.loanApplicationId, true)
                }
              >
                승인하기
              </Button>
            </div>
          ) : (
            <Button className='mt-6' onClick={closeModal}>
              확인
            </Button>
          )}
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
