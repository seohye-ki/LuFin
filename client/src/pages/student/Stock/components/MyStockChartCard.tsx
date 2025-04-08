import StockChart from '../../../../components/Graph/StockChart';
import Lufin from '../../../../components/Lufin/Lufin';
import { useStockStore } from '../../../../libs/store/stockStore';
import { calcProfitRate } from '../../../../libs/utils/stock-util';

const MyStockChartCard = () => {
  const { products, portfolio } = useStockStore.getState();

  const stockDetails = portfolio
    .map((p) => {
      const stock = products.find((s) => s.StockProductId === p.StockProductId);
      if (!stock) return null;

      const avgPrice = p.Quantity > 0 ? p.TotalPurchaseAmount / p.Quantity : 0;
      const currentPrice = stock.CurrentPrice * p.Quantity;
      const { profitValue, profitRate } = calcProfitRate(p.TotalPurchaseAmount, currentPrice);

      return {
        stock: stock.Name,
        quantity: p.Quantity,
        amount: avgPrice,
        currentPrice,
        profit: profitValue,
        profitRate,
      };
    })
    .filter((detail): detail is NonNullable<typeof detail> => detail !== null);

  const totalAmount = stockDetails.reduce((sum, stock) => sum + stock.currentPrice, 0);

  return (
    <div className='flex flex-col items-center justify-center gap-4'>
      <div className='relative w-full max-w-[300px] aspect-[3/2]'>
        <StockChart
          stocks={stockDetails.map((s) => ({ stock: s.stock, amount: s.currentPrice }))}
          className='absolute top-0 left-0 w-full h-full'
        />
      </div>
      <span className='text-p3 text-gray-500 flex items-center gap-1'>
        총 보유 자산은{' '}
        <span className='text-black font-bold'>
          <Lufin size='s' count={totalAmount} />
        </span>
        입니다.
      </span>
    </div>
  );
};

export default MyStockChartCard;
