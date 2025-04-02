import StockChart from '../../../../components/Graph/StockChart';
import Lufin from '../../../../components/Lufin/Lufin';
import { calcProfitRate } from '../../../../libs/utils/stock-util';
import { stockPortfolios, stockProducts } from '../../../../types/Stock/stock';
import { members } from '../../../../types/Member/member';

const MyStockChart = () => {
  const memberId = 2; // ToDo: 회원 아이디 받아오기

  const stockPortfolio = stockPortfolios.filter((portpolio) => portpolio.memberId === memberId);

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

  const totalAmount = stockDetails.reduce((sum, stock) => sum + stock.currentPrice, 0);

  return (
    <div className='flex w-full gap-8 rounded-xl bg-white px-4'>
      {/* 차트 영역 */}
      <div className='w-[50%] flex flex-col items-center justify-center gap-4'>
        <div className='relative w-full max-w-[300px] aspect-[3/2]'>
          <StockChart
            stocks={stockDetails.map((stock) => ({
              stock: stock.stock!,
              amount: stock.currentPrice,
            }))}
            className='absolute top-0 left-0 w-full h-full'
          />
        </div>
        <span className='text-p3 text-gray-500 flex items-center gap-1'>
          {members.find((member) => member.memberId === memberId)?.name}님의 보유 주식은 총{' '}
          <span className='text-black font-bold'>
            <Lufin size='s' count={totalAmount} />
          </span>
          이에요.
        </span>
      </div>

      {/* 종목별 상세 정보 */}
      <div className='w-[50%] flex flex-col justify-start gap-4'>
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
    </div>
  );
};

export default MyStockChart;
