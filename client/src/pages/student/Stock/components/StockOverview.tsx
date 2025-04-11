import Button from '../../../../components/Button/Button';
import Card from '../../../../components/Card/Card';
import TableView from '../../../../components/Frame/TableView';
import Lufin from '../../../../components/Lufin/Lufin';
import MyStockChart from './MyStockChart';
import { calcStockChangeRate } from '../../../../libs/utils/stock-util';
import StockChangeText from './stockChangeText';
import { useSelectedStockStore, useStockStore } from '../../../../libs/store/stockStore';

const StockOverview = () => {
  const { setSelectedStock, chartType, toggleChartType } = useSelectedStockStore();
  const { products } = useStockStore();

  const columns = [
    { key: 'id', label: 'NO' },
    { key: 'name', label: '종목' },
    { key: 'currentPrice', label: '현재가' },
    { key: 'changeRate', label: '등락률' },
  ];

  const rows = products.map((stock) => {
    const { diff, rate } = calcStockChangeRate(stock.currentPrice, stock.initialPrice);

    return {
      id: stock.stockProductId,
      name: stock.name,
      currentPrice: <Lufin size='s' count={stock.currentPrice} />,
      changeRate: <StockChangeText diff={diff} rate={rate} />,
    };
  });

  const handleRowClick = (row: { [key: string]: React.ReactNode }) => {
    const id = Number(row.id);
    const stock = products.find((s) => s.stockProductId === id);
    if (stock) setSelectedStock(stock);
  };

  return chartType === 'portfolio' ? (
    <MyStockChart />
  ) : (
    <Card
      titleLeft='종목 현황'
      titleRight={
        <div className='flex items-center gap-2'>
          <div className='text-c1 text-grey'>
            {products.length > 0
              ? new Date(products[0].createdAt).toLocaleString('ko-KR', {
                  year: 'numeric',
                  month: '2-digit',
                  day: '2-digit',
                  hour: '2-digit',
                  minute: '2-digit',
                })
              : ''}
          </div>
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
