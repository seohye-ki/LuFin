import { useState } from 'react';
import Button from '../../../../components/Button/Button';
import Card from '../../../../components/Card/Card';
import Checkbox from '../../../../components/Form/Checkbox';
import TableView, { TableColumn, TableRow } from '../../../../components/Frame/TableView';
import Profile from '../../../../components/Profile/Profile';
import { ItemRequestDTO } from '../../../../types/shop/item';
import { showGlobalAlert, hideGlobalAlert } from '../../../../libs/store/alertStore';
import { approveItemRequest } from '../../../../libs/services/shop/shop.service';

const ItemUseRequest: React.FC<{ itemReuqestList: ItemRequestDTO[]; closeModal: () => void }> = ({
  itemReuqestList,
  closeModal,
}) => {
  const [selectedRequestList, setSelectedRequestList] = useState<{ [requestId: number]: boolean }>(
    {},
  );

  const selectedIds = Object.keys(selectedRequestList)
    .filter((id) => selectedRequestList[Number(id)])
    .map(Number);

  const checkOne = (requestId: number) => {
    setSelectedRequestList((prev) => ({
      ...prev,
      [requestId]: !prev[requestId],
    }));
  };

  const checkAll = () => {
    const hasAnySelection = Object.values(selectedRequestList).some((isSelected) => isSelected);

    if (hasAnySelection) {
      setSelectedRequestList({});
    } else {
      const newSelections: { [requestId: number]: boolean } = {};
      itemReuqestList.forEach((req) => {
        newSelections[req.requestId] = true;
      });
      setSelectedRequestList(newSelections);
    }
  };

  const handleApproval = (isApprove: boolean) => {
    if (selectedIds.length === 0) return;

    showGlobalAlert(
      isApprove ? '아이템 사용을 승인하시겠습니까?' : '아이템 사용을 거절하시겠습니까?',
      null,
      isApprove ? '선택한 학생들에게 아이템이 지급됩니다.' : '선택한 요청은 거절 처리됩니다.',
      'info',
      {
        label: '확인',
        onClick: async () => {
          await Promise.all(selectedIds.map((id) => approveItemRequest(id, isApprove)));

          showGlobalAlert(
            isApprove ? '승인이 완료되었습니다.' : '거절이 완료되었습니다.',
            null,
            '',
            'info',
            {
              label: '확인',
              onClick: () => {
                hideGlobalAlert();
                closeModal();
              },
            },
          );
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

  const columns: TableColumn[] = [
    {
      key: 'select',
      label: (
        <Checkbox
          id='select-all'
          checked={Object.values(selectedRequestList).some((v) => v === true)}
          onChange={checkAll}
        />
      ),
    },
    { key: 'name', label: '아이템' },
    { key: 'student', label: '학생' },
  ];

  const rows: TableRow[] = itemReuqestList.map((itemRequest) => ({
    select: (
      <div onClick={(e) => e.stopPropagation()}>
        <Checkbox
          id={`checkbox-${itemRequest.requestId}`}
          checked={selectedRequestList[itemRequest.requestId] || false}
          onChange={() => checkOne(itemRequest.requestId)}
        />
      </div>
    ),
    name: itemRequest.itemName,
    student: (
      <Profile name={itemRequest.memberName} profileImage={itemRequest.memberProfileImage} />
    ),
  }));

  return (
    <div
      className='fixed top-0 left-0 right-0 bottom-0 flex justify-center items-center bg-black/80 z-50'
      onClick={closeModal}
    >
      <div onClick={(e) => e.stopPropagation()}>
        <Card className='w-104 h-160' titleLeft='아이템 사용 신청 목록'>
          <div className='overflow-auto h-full'>
            <TableView columns={columns} rows={rows} />
          </div>

          <div className='flex flex-row gap-4 mt-4'>
            <Button color='danger' className='w-full' onClick={() => handleApproval(false)}>
              거절하기
            </Button>
            <Button color='primary' className='w-full' onClick={() => handleApproval(true)}>
              승인하기
            </Button>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default ItemUseRequest;
