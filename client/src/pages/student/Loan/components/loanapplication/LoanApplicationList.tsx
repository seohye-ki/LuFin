import React from 'react';
import { LoanApplicationDTO, LoanApplicationListDTO } from '../../../../../types/Loan/loan';
import TableView, { TableColumn, TableRow } from '../../../../../components/Frame/TableView';
import Badge from '../../../../../components/Badge/Badge';
import Lufin from '../../../../../components/Lufin/Lufin';
import Card from '../../../../../components/Card/Card';
import { dateUtil } from '../../../../../libs/utils/date-util';

type LoanApplicationListProps = {
  loanApplicationList: LoanApplicationListDTO;
  showMemberName?: boolean;
  onRowClick: (loanApplication: LoanApplicationDTO) => void;
};

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

const LoanApplicationList: React.FC<LoanApplicationListProps> = ({
  loanApplicationList,
  showMemberName = true,
  onRowClick,
}) => {
  const columns: TableColumn[] = [
    ...(showMemberName ? [{ key: 'member', label: '이름' }] : []),
    { key: 'product', label: '상품명' },
    { key: 'requiredAmount', label: '대출금액' },
    { key: 'startDate', label: '시작일' },
    { key: 'dueDate', label: '만기일' },
    { key: 'status', label: '상태' },
  ];

  const rows: TableRow[] = (loanApplicationList ?? []).map((loanApplication) => {
    const { badgeStatus, label } = statusMapping[loanApplication.status] || {
      badgeStatus: 'ready',
      label: '알 수 없음',
    };

    return {
      id: loanApplication.loanApplicationId as number,
      ...(showMemberName && { member: loanApplication.memberName }),
      product: loanApplication.productName,
      requiredAmount: <Lufin size='s' count={loanApplication.requiredAmount} />,
      startDate:
        loanApplication.status === 'PENDING'
          ? '신청 중'
          : dateUtil(loanApplication.startedAt).formattedDate,
      dueDate:
        loanApplication.status === 'PENDING'
          ? '신청 중'
          : dateUtil(loanApplication.dueDate).formattedDate,
      status: <Badge status={badgeStatus}>{label}</Badge>,
    };
  });

  return (
    <Card titleLeft='대출 내역' className='min-h-0 h-full'>
      <TableView
        columns={columns}
        rows={rows}
        onRowClick={(row) => {
          if (onRowClick && row.id !== undefined) {
            const selected = loanApplicationList.find((item) => item.loanApplicationId === row.id);
            if (selected) {
              onRowClick(selected);
            }
          }
        }}
      />
    </Card>
  );
};

export default LoanApplicationList;
