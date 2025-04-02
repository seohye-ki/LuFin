import StockChart from '../../../../components/Graph/StockChart';
import Lufin from '../../../../components/Lufin/Lufin';
import { calcProfitRate } from '../../../../libs/utils/stock-util';
import { members } from '../../../../types/member/member';
import { stockPortfolios, stockProducts } from '../../../../types/Stock/stock';

const MyStockChartCard = () => {
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

  const totalAmount = stockDetails.reduce((sum, stock) => sum + stock.currentPrice, 0);

  return (
    <div className='flex flex-col items-center justify-center gap-4'>
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
  );
};

export default MyStockChartCard;
