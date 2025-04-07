import DashboardCard from './DashboardCard';

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
        <div className='flex-1 min-h-0 max-h-[calc(100%-3rem)] overflow-y-auto scrollbar-hide'>
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
              <div className='flex items-center justify-center h-20'>
                <span className='text-p1 text-grey'>보유한 아이템이 없습니다.</span>
              </div>
            )}
          </div>
        </div>
      </div>
    </DashboardCard>
  );
};

export default ItemList;
