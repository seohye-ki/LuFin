import SidebarLayout from '../../../components/Layout/SidebarLayout';
import TodayNews from '../../student/Stock/components/TodayNews';
import StockInfoNotice from '../../student/Stock/components/StockInfoNotice';
import StockOverview from '../../student/Stock/components/StockOverview';
import StockDetailView from '../../student/Stock/components/StockDetailView';
import { useStockStore } from '../../../libs/store/stockStore';

const TeacherStock = () => {
  const { selectedStock } = useStockStore();

  return (
    <SidebarLayout>
      <div className='flex flex-col h-full gap-3'>
        <StockInfoNotice />
        {selectedStock ? <StockDetailView /> : <StockOverview />}
        <TodayNews />
      </div>
    </SidebarLayout>
  );
};

export default TeacherStock;
