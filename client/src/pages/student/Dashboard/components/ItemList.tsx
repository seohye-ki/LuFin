import { Link } from 'react-router-dom';
import DashboardCard from './DashboardCard';
import { paths } from '../../../../routes/paths';

interface ItemInfo {
  name: string;
  count: number;
  daysLeft: number;
}

interface ItemListProps {
  items: ItemInfo[];
}

const ItemList = ({ items }: ItemListProps) => {
  return (
    <DashboardCard titleLeft='아이템' className='flex-1'>
      <div className='h-full flex flex-col'>
        <div className='flex-1 min-h-0  overflow-y-auto scrollbar-hide'>
          <div className='flex flex-col gap-3 px-1 py-1'>
            {items.length > 0 ? (
              items.map((item, index) => (
                <div
                  key={`${item.name}-${index}`}
                  className='flex justify-between items-center p-3 bg-broken-white rounded-lg'
                >
                  <div className='flex items-center gap-3'>
                    <span className='text-p1 font-medium'>{item.name}</span>
                    <span className='text-p2 text-grey'>{item.daysLeft}일 남음</span>
                  </div>
                  <div className='bg-purple px-2.5 py-1 rounded-full flex items-center'>
                    <span className='text-p2 font-semibold text-black'>{item.count}개</span>
                  </div>
                </div>
              ))
            ) : (
              <div className='flex flex-col items-center justify-center h-22 gap-2'>
                <p className='text-p2 text-grey'>사용가능한 아이템이 없어요.</p>
                <Link to={paths.SHOP} className='text-c1 text-info hover:underline'>
                  아이템 구매하러 가기
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </DashboardCard>
  );
};

export default ItemList;
