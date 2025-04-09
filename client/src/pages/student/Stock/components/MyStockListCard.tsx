import Lufin from '../../../../components/Lufin/Lufin';
import Button from '../../../../components/Button/Button';
import { calcProfitRate } from '../../../../libs/utils/stock-util';
import Card from '../../../../components/Card/Card';
import { useStockStore } from '../../../../libs/store/stockStore';
import { useSelectedStockStore } from '../../../../libs/store/stockStore';
const MyStockListCard = () => {
  const { products, portfolio } = useStockStore();
  const { toggleChartType } = useSelectedStockStore();

  const stockDetails = portfolio
    .map((p) => {
      const stock = products.find((s) => s.stockProductId === p.stockProductId);
      if (!stock) return null;

      const avgPrice = p.quantity > 0 ? p.totalPurchaseAmount / p.quantity : 0;
      const currentPrice = stock.currentPrice * p.quantity;
      const { profitValue, profitRate } = calcProfitRate(p.totalPurchaseAmount, currentPrice);

      return {
        stock: stock.name,
        quantity: p.quantity,
        amount: avgPrice,
        currentPrice,
        profit: profitValue,
        profitRate,
      };
    })
    .filter((detail): detail is NonNullable<typeof detail> => detail !== null);

  return (
    <Card
      titleLeft='나의 투자 현황'
      titleRight={
        <Button variant='solid' size='sm' color='primary' onClick={toggleChartType}>
          종목 현황
        </Button>
      }
    >
      <div className='flex flex-col justify-start gap-4 p-4'>
        {stockDetails.length === 0 ? (
          <div className='text-gray-400 text-sm'>보유 주식이 없습니다.</div>
        ) : (
          stockDetails.map((stock) => (
            <div key={stock.stock} className='flex flex-col gap-2'>
              <div className='flex justify-between'>
                <span className='text-p1 font-bold'>{stock.stock}</span>
                <Lufin size='s' count={stock.currentPrice} />
              </div>
              <div className='flex justify-between'>
                <span className='text-p3 text-gray-500'>{stock.quantity}주</span>
                <span
                  className={
                    stock.profit > 0
                      ? 'text-p3 text-red-500'
                      : stock.profit < 0
                        ? 'text-p3 text-blue-500'
                        : 'text-p3 text-gray-500'
                  }
                >
                  {stock.profit > 0 ? '+' : ''}
                  {stock.profit.toLocaleString()}루핀 ({stock.profitRate}%)
                </span>
              </div>
            </div>
          ))
        )}
      </div>
    </Card>
  );
};

export default MyStockListCard;
