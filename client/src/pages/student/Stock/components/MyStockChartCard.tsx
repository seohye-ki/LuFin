import StockChart from '../../../../components/Graph/StockChart';
import Lufin from '../../../../components/Lufin/Lufin';
import { useStockStore } from '../../../../libs/store/stockStore';
import { buildPortfolioDetails } from '../../../../libs/utils/stock-util';

const MyStockChartCard = () => {
  const { products, portfolio } = useStockStore();

  const stockDetails = buildPortfolioDetails(portfolio, products);

  const totalAmount = stockDetails.reduce((sum, stock) => sum + stock.currentValue, 0);

  return (
    <div className='flex flex-col items-center justify-center gap-4'>
      <div className='relative w-full max-w-[300px] aspect-[3/2]'>
        <StockChart
          stocks={stockDetails.map((s) => ({ stock: s.stock, amount: s.currentValue }))}
          className='absolute top-0 left-0 w-full h-full'
        />
      </div>
      <span className='text-p3 text-gray-500 flex items-center gap-1'>
        총 보유 주식은{' '}
        <span className='text-black font-bold'>
          <Lufin size='s' count={totalAmount} />
        </span>
        입니다.
      </span>
    </div>
  );
};

export default MyStockChartCard;
