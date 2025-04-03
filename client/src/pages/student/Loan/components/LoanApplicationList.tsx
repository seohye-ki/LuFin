import React from 'react';
import { LoanApplicationListDTO } from '../../../../types/Loan/loan';
import TableView, { TableColumn, TableRow } from '../../../../components/Frame/TableView';
import Badge from '../../../../components/Badge/Badge';
import Lufin from '../../../../components/Lufin/Lufin';

type LoanApplicationListProps = {
  loanApplications: LoanApplicationListDTO;
  showMemberName?: boolean;
  onRowClick?: (loanApplicationId: number) => void;
};

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

const LoanApplicationList: React.FC<LoanApplicationListProps> = ({
  loanApplications,
  showMemberName = true,
  onRowClick,
}) => {
  // 컬럼 설정 (이름 컬럼 포함 여부에 따라 동적으로 구성)
  const columns: TableColumn[] = [
    ...(showMemberName ? [{ key: 'member', label: '이름' }] : []),
    { key: 'product', label: '상품명' },
    { key: 'requiredAmount', label: '대출금액' },
    { key: 'startDate', label: '시작일' },
    { key: 'dueDate', label: '만기일' },
    { key: 'status', label: '상태' },
  ];

  const rows: TableRow[] = loanApplications.map((loanApplication) => {
    const { badgeStatus, label } = statusMapping[loanApplication.status] || {
      badgeStatus: 'ready',
      label: '알 수 없음',
    };

    return {
      id: loanApplication.loanApplicationId as number, // `id`가 확실히 존재하도록 설정
      ...(showMemberName && { member: loanApplication.memberName }),
      product: loanApplication.loanProductName,
      requiredAmount: <Lufin size='s' count={loanApplication.requiredAmount} />,
      startDate: loanApplication.createdAt.formattedDate,
      dueDate: loanApplication.dueDate.formattedDate,
      status: <Badge status={badgeStatus}>{label}</Badge>,
    };
  });

  return (
    <TableView
      columns={columns}
      rows={rows}
      onRowClick={(row) => {
        if (onRowClick && row.id !== undefined) {
          onRowClick(row.id as number); // 명확한 타입 캐스팅 추가
        }
      }}
    />
  );
};

export default LoanApplicationList;
