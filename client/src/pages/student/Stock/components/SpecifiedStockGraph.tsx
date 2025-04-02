import StockGraph from '../../../../components/Graph/StockGraph';
import Button from '../../../../components/Button/Button';
import StockOrder from './StockOrder';
import { dateUtil } from '../../../../libs/utils/date-util';
import { stockPriceHistories, StockProduct } from '../../../../types/Stock/stock';
import { useMemo } from 'react';

interface SpecifiedStockGraphProps {
  stock: StockProduct;
  onBack: () => void;
}

const SpecifiedStockGraph = ({ stock, onBack }: SpecifiedStockGraphProps) => {
  const stockPriceInfos = useMemo(() => {
    return stockPriceHistories
      .filter((history) => history.stockProductId === stock.stockProductId)
      .map((h) => ({
        date: dateUtil(h.createdAt),
        price: h.unitPrice,
      }));
  }, [stock]);

  return (
    <div className='flex h-full gap-3'>
      {/* 차트 영역 */}
      <div className='flex flex-col bg-white rounded-xl p-4 w-[65%]'>
        <div className='flex justify-between items-center mb-3'>
          <h2 className='text-h3 font-semibold text-black'>{stock.name}</h2>
          <Button size='sm' variant='solid' onClick={onBack}>
            뒤로가기
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
      <div className='bg-white rounded-xl w-[35%] min-w-[260px] min-h-0'>
        <StockOrder stock={stock} />
      </div>
    </div>
  );
};

export default SpecifiedStockGraph;
