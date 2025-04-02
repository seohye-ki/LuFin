import { useStockStore } from '../../../../libs/store/stockStore';
import SpecifiedStockGraph from './SpecifiedStockGraph';

const StockDetailView = () => {
  const { selectedStock, setSelectedStock } = useStockStore();

  if (!selectedStock) return null;

  return (
    <div className='flex flex-col basis-45/100 min-h-0 gap-3'>
      <SpecifiedStockGraph stock={selectedStock} onBack={() => setSelectedStock(null)} />
    </div>
  );
};

export default StockDetailView;
