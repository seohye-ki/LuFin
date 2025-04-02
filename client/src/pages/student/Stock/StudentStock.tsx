import SidebarLayout from '../../../components/Layout/SidebarLayout';
import TodayNews from './components/TodayNews';
import StockInfoNotice from './components/StockInfoNotice';
import StockOverview from './components/StockOverview';
import StockDetailView from './components/StockDetailView';
import { useStockStore } from '../../../libs/store/stockStore';

const StudentStock = () => {
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

export default StudentStock;
