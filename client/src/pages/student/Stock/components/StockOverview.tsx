import Button from '../../../../components/Button/Button';
import Card from '../../../../components/Card/Card';
import TableView from '../../../../components/Frame/TableView';
import Lufin from '../../../../components/Lufin/Lufin';
import { useStockStore } from '../../../../libs/store/stockStore';
import { stockProducts } from '../../../../types/stock/stock';
import MyStockChart from './MyStockChart';
import { calcStockChangeRate } from '../../../../libs/utils/stock-util';
import StockChangeText from './stockChangeText';

const StockOverview = () => {
  const { chartType, toggleChartType, setSelectedStock } = useStockStore();

  const columns = [
    { key: 'id', label: 'NO' },
    { key: 'name', label: '종목' },
    { key: 'currentPrice', label: '현재가' },
    { key: 'changeRate', label: '등락률' },
  ];

  const rows = stockProducts.map((stock) => {
    const { diff, rate } = calcStockChangeRate(stock.currentPrice, stock.initialPrice);

    return {
      id: stock.stockProductId.toString(),
      name: stock.name,
      currentPrice: <Lufin size='s' count={stock.currentPrice} />,
      changeRate: <StockChangeText diff={diff} rate={rate} />,
    };
  });

  const handleRowClick = (row: { [key: string]: React.ReactNode }) => {
    const id = Number(row.id);
    const stock = stockProducts.find((s) => s.stockProductId === id);
    if (stock) setSelectedStock(stock);
  };

  return chartType === 'portfolio' ? (
    <MyStockChart />
  ) : (
    <Card
      titleLeft='실시간 차트'
      titleRight={
        <div className='flex items-center gap-2'>
          <div className='text-c1 text-grey'>2025.03.31 13:00 기준</div>
          <Button variant='solid' size='sm' color='primary' onClick={toggleChartType}>
            나의 투자 현황
          </Button>
        </div>
      }
      className='flex flex-col basis-45/100 min-h-0'
    >
      <div className='flex-1 overflow-auto [&::-webkit-scrollbar]:hidden'>
        <TableView columns={columns} rows={rows} onRowClick={handleRowClick} />
      </div>
    </Card>
  );
};

export default StockOverview;
