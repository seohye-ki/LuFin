import Lufin from '../../../../components/Lufin/Lufin';
import Button from '../../../../components/Button/Button';
import { calcProfitRate } from '../../../../libs/utils/stock-util';
import { stockPortfolios, stockProducts } from '../../../../types/stock/stock';
import Card from '../../../../components/Card/Card';
import { useStockStore } from '../../../../libs/store/stockStore';

const MyStockListCard = () => {
  const { toggleChartType } = useStockStore();
  const memberId = 2; // ToDo: 회원 아이디 받아오기
  const stockPortfolio = stockPortfolios.filter((portfolio) => portfolio.memberId === memberId);

  const stockDetails = stockPortfolio
    .map((portfolio) => {
      const stock = stockProducts.find(
        (stock) => stock.stockProductId === portfolio.stockProductId,
      );
      if (!stock) return null;
      const averagePrice =
        portfolio.quantity > 0 ? portfolio.totalPurchaseAmount / portfolio.quantity : 0;
      const currentPrice = stock.currentPrice * portfolio.quantity;
      const { profitValue, profitRate } = calcProfitRate(
        portfolio.totalPurchaseAmount,
        currentPrice,
      );
      return {
        stock: stock.name,
        quantity: portfolio.quantity,
        amount: averagePrice,
        currentPrice: currentPrice,
        profit: profitValue,
        profitRate: profitRate,
      };
    })
    .filter((detail): detail is NonNullable<typeof detail> => detail !== null);

  return (
    <Card
      titleLeft='나의 투자 현황'
      titleRight={
        <Button variant='solid' size='sm' color='primary' onClick={toggleChartType}>
          실시간 차트
        </Button>
      }
    >
      <div className='flex flex-col justify-start gap-4 p-4'>
        {stockDetails.length === 0 ? (
          <div className='text-gray-400 text-sm'>보유 주식이 없습니다.</div>
        ) : (
          stockDetails.map((stock) => (
            <div
              key={stock.stock}
              className='flex flex-col gap-2 overflow-y-auto [&::-webkit-scrollbar]:hidden'
            >
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
                        : ''
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
