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
import { initialItemPreviews, ItemPreview } from '../../../types/item';

const TeacherShop = () => {
  const [itemPreviews, setItemPreviews] = useState<ItemPreview[]>([]);
  const [showCreateModal, setShowCreateModal] = useState<boolean>(false);
  const [selectedItems, setSelectedItems] = useState<{ [itemId: number]: boolean }>({});
  const [selectedItem, setSelectedItem] = useState<ItemPreview | null>(null);

  useEffect(() => {
    setItemPreviews(initialItemPreviews);
  });

  const clickRow = (row: TableRow) => {
    const item = itemPreviews.find((item) => item.itemId === row.id);
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
    const newSelections: { [itemId: number]: boolean } = {};

    itemPreviews.forEach((item) => {
      newSelections[item.itemId] = true;
    });

    setSelectedItems(newSelections);
  };

  const deleteItems = () => {
    const itemsToDelete = Object.keys(selectedItems)
      .filter((itemId) => selectedItems[parseInt(itemId)])
      .map((itemId) => parseInt(itemId));

    if (itemsToDelete.length > 0) {
      const filteredItems = itemPreviews.filter((item) => !itemsToDelete.includes(item.itemId));
      setItemPreviews(filteredItems);
      setSelectedItems({});
    }
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
          checked={Object.keys(selectedItems).length === itemPreviews.length}
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

  const rows: TableRow[] = itemPreviews.map((item) => ({
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
    name: item.name,
    price: <Lufin count={item.price} size='s' />,
    quantity: item.quantityAvailable - item.quantitySold,
    status: (
      <Badge status={item.status ? 'ing' : 'done'}>{item.status ? '판매중' : '판매종료'}</Badge>
    ),
    dueDate: item.expirationDate.formattedDate,
  }));

  return (
    <SidebarLayout userRole='teacher'>
      <Card
        titleRight={
          <div className='flex flex-row items-center gap-2'>
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

      {showCreateModal && <CreateModal closeModal={closeModal} />}

      {selectedItem && <ItemDetailModal item={selectedItem} closeModal={closeModal} />}
    </SidebarLayout>
  );
};

export default TeacherShop;
