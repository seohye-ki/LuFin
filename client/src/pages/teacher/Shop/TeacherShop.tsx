import { useEffect, useState } from 'react';
import TableView, { TableColumn, TableRow } from '../../../components/Frame/TableView';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import Card from '../../../components/Card/Card';
import Lufin from '../../../components/Lufin/Lufin';
import Badge from '../../../components/Badge/Badge';
import Checkbox from '../../../components/Form/Checkbox';
import { Icon } from '../../../components/Icon/Icon';
import CreateModal from './components/CreateModal';
import ItemDetailModal from './components/ItemDetailModal';
import { ItemDTO, ItemRequestDTO } from '../../../types/shop/item';
import { dateUtil } from '../../../libs/utils/date-util';
import {
  deleteItem,
  getItemRequestList,
  getSalesItemList,
} from '../../../libs/services/shop/shop.service';
import Button from '../../../components/Button/Button';
import ItemUseRequest from './components/ItemUseRequest';

const TeacherShop = () => {
  const [itemList, setItemList] = useState<ItemDTO[]>([]);
  const [itemRequestList, setItemRequestList] = useState<ItemRequestDTO[]>([]);
  const [showCreateModal, setShowCreateModal] = useState<boolean>(false);
  const [selectedItems, setSelectedItems] = useState<{ [itemId: number]: boolean }>({});
  const [selectedItem, setSelectedItem] = useState<ItemDTO | null>(null);
  const [showItemUseRequestModal, setShowItemUseRequestModal] = useState(false);

  const fetchItemList = async () => {
    const res = await getSalesItemList();
    setItemList(res);
  };

  const fetchRequestList = async () => {
    const res = await getItemRequestList();
    setItemRequestList(res);
  };

  useEffect(() => {
    fetchItemList();
    fetchRequestList();
  }, []);

  const handleSuccess = () => {
    fetchItemList();
    setShowCreateModal(false);
  };

  const clickRow = (row: TableRow) => {
    const item = itemList.find((item) => item.itemId === row.id);
    if (item) {
      setSelectedItem(item);
    }
  };

  const checkOne = (itemId: number) => {
    setSelectedItems((selected) => ({
      ...selected,
      [itemId]: !selected[itemId],
    }));
  };

  const checkAll = () => {
    const hasAnySelection = Object.values(selectedItems).some((isSelected) => isSelected);

    if (hasAnySelection) {
      setSelectedItems({});
    } else {
      const newSelections: { [itemId: number]: boolean } = {};

      itemList.forEach((item) => {
        newSelections[item.itemId] = true;
      });

      setSelectedItems(newSelections);
    }
  };

  const deleteItems = () => {
    const itemsToDelete = Object.keys(selectedItems)
      .filter((itemId) => selectedItems[parseInt(itemId)])
      .map((itemId) => parseInt(itemId));

    itemsToDelete.forEach(async (itemId: number) => {
      await deleteItem(itemId);
    });

    fetchItemList();
  };

  const closeModal = () => {
    setShowCreateModal(false);
    console.log(showCreateModal);
    setSelectedItem(null);
  };

  const columns: TableColumn[] = [
    {
      key: 'select',
      label: (
        <Checkbox
          id='select-all'
          checked={Object.values(selectedItems).some((v) => v === true)}
          onChange={checkAll}
        />
      ),
    },
    { key: 'name', label: '이름' },
    { key: 'price', label: '가격' },
    { key: 'quantity', label: '수량' },
    { key: 'status', label: '상태' },
    { key: 'dueDate', label: '마감일' },
  ];

  const rows: TableRow[] = itemList.map((item) => ({
    select: (
      <div onClick={(e) => e.stopPropagation()}>
        <Checkbox
          id={`checkbox-${item.itemId}`}
          checked={selectedItems[item.itemId] || false}
          onChange={() => checkOne(item.itemId)}
        />
      </div>
    ),
    id: item.itemId,
    name: item.itemName,
    price: <Lufin count={item.price} size='s' />,
    quantity: item.quantityAvailable - item.quantitySold,
    status: (
      <Badge status={item.status ? 'ing' : 'done'}>{item.status ? '판매중' : '판매종료'}</Badge>
    ),
    dueDate: dateUtil(item.expirationDate).formattedDate,
  }));

  return (
    <SidebarLayout>
      <Card
        titleLeft='아이템 목록'
        titleRight={
          <div className='flex flex-row items-center gap-2'>
            {itemRequestList.length > 0 && (
              <div className='relative inline-block'>
                <Button onClick={() => setShowItemUseRequestModal(true)}>아이템 사용 신청</Button>
                <span className='absolute top-1 right-2 w-2.5 h-2.5 bg-red-500 rounded-full border-2 border-white' />
              </div>
            )}
            <button
              className='flex items-center justify-center'
              onClick={() => setShowCreateModal(true)}
            >
              <Icon name='CircleAdd' size={42} />
            </button>
            <button className='flex items-center justify-center' onClick={deleteItems}>
              {Object.keys(selectedItems).length > 0 && <Icon name='CircleTrash' size={42} />}
            </button>
          </div>
        }
        className='w-full h-full'
      >
        <TableView columns={columns} rows={rows} onRowClick={clickRow} />
      </Card>

      {showCreateModal && <CreateModal closeModal={closeModal} onSuccess={handleSuccess} />}

      {selectedItem && <ItemDetailModal item={selectedItem} closeModal={closeModal} />}

      {showItemUseRequestModal && (
        <ItemUseRequest
          itemReuqestList={itemRequestList}
          closeModal={() => {
            fetchRequestList();
            setShowItemUseRequestModal(false);
          }}
        />
      )}
    </SidebarLayout>
  );
};

export default TeacherShop;
