import StockGraph from '../../../../components/Graph/StockGraph';
import Button from '../../../../components/Button/Button';
import StockOrder from './StockOrder';
import { dateUtil } from '../../../../libs/utils/date-util';
import { StockProduct } from '../../../../types/stock/stock';
import { useMemo } from 'react';
import { useStockStore } from '../../../../libs/store/stockStore';

interface SpecifiedStockGraphProps {
  stock: StockProduct;
  onBack: () => void;
}

const SpecifiedStockGraph = ({ stock, onBack }: SpecifiedStockGraphProps) => {
  const { priceHistory } = useStockStore();
  const stockPriceInfos = useMemo(() => {
    return priceHistory.map((h) => ({
      date: dateUtil(h.CreatedAt),
      price: h.UnitPrice,
    }));
  }, [priceHistory]);

  return (
    <div className='flex h-full gap-3'>
      {/* 차트 영역 */}
      <div className='flex flex-col bg-white rounded-xl p-4 w-[70%]'>
        <div className='flex justify-between items-center mb-3'>
          <h2 className='text-h3 font-semibold text-black'>{stock.Name}</h2>
          <Button size='sm' variant='solid' onClick={onBack}>
            종목 현황
          </Button>
        </div>
        <div className='flex-1 min-h-0'>
          {stockPriceInfos.length === 0 ? (
            <div className='flex items-center justify-center h-full'>
              <span className='text-p2 text-grey'>거래 내역이 없습니다.</span>
            </div>
          ) : (
            <StockGraph stockPriceInfos={stockPriceInfos} />
          )}
        </div>
      </div>

      {/* 주문 영역 */}
      <div className='bg-white rounded-xl w-[30%] min-w-[260px] min-h-0'>
        <StockOrder stock={stock} />
      </div>
    </div>
  );
};

export default SpecifiedStockGraph;
