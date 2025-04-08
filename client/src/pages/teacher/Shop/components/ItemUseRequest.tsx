import { useState } from 'react';
import Button from '../../../../components/Button/Button';
import Card from '../../../../components/Card/Card';
import Checkbox from '../../../../components/Form/Checkbox';
import TableView, { TableColumn, TableRow } from '../../../../components/Frame/TableView';
import Profile from '../../../../components/Profile/Profile';
import { ItemRequestDTO } from '../../../../types/shop/item';

const ItemUseRequest: React.FC<{ itemReuqestList: ItemRequestDTO[]; closeModal: () => void }> = ({
  itemReuqestList,
  closeModal,
}) => {
  const [selectedRequestList, setSelectedRequestList] = useState<{ [requestId: number]: boolean }>(
    {},
  );

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
          <div className='overflow-auto'>
            <TableView columns={columns} rows={rows} />
          </div>

          <div className='flex flex-row gap-4 mt-4'>
            <Button color='neutral' className='w-full' onClick={closeModal}>
              취소하기
            </Button>
            <Button className='w-full'>승인하기</Button>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default ItemUseRequest;
