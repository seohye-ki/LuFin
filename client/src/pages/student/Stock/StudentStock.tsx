import SidebarLayout from '../../../components/Layout/SidebarLayout';
import Card from '../../../components/Card/Card';
import Button from '../../../components/Button/Button';
import TableView from '../../../components/Frame/TableView';
import { stockProducts, StockProduct } from '../../../types/Stock/stock';
import Lufin from '../../../components/Lufin/Lufin';
import TodayNews from './components/TodayNews';
import MyStockChart from './components/MyStockChart';
import { useState } from 'react';
import { Icon } from '../../../components/Icon/Icon';
import SpecifiedStockGraph from './components/SpecifiedStockGraph';
import { useStockStore } from '../../../libs/store/stockStore';
import StockInfoNotice from './components/StockInfoNotice';
const StudentStock = () => {
  type TableRow = {
    [key: string]: React.ReactNode;
  };

  const handleRowClick = (row: TableRow) => {
    const id = Number(row.id);
    const stock = stockProducts.find((s) => s.stockProductId === id);
    setSelectedStock(stock || null);
  };

  const columns = [
    { key: 'id', label: 'NO' },
    { key: 'name', label: '종목' },
    { key: 'currentPrice', label: '현재가' },
    { key: 'changeRate', label: '등락률' },
  ];

  const rows = stockProducts.map((stock) => ({
    id: stock.stockProductId.toString(),
    name: stock.name,
    currentPrice: <Lufin size='s' count={stock.currentPrice} />,
    changeRate: (
      <span className={stock.currentPrice - stock.initialPrice > 0 ? 'text-red-500' : 'text-info'}>
        {`${stock.currentPrice - stock.initialPrice > 0 ? '+' : ''}${stock.currentPrice - stock.initialPrice}루핀 (${(
          ((stock.currentPrice - stock.initialPrice) / stock.initialPrice) *
          100
        ).toFixed(2)}%)`}
      </span>
    ),
  }));

  const [showMyStockChart, setShowMyStockChart] = useState(false);
  const [selectedStock, setSelectedStock] = useState<StockProduct | null>(null);

  return (
    <SidebarLayout>
      <div className='flex flex-col h-full gap-3'>
        {selectedStock ? (
          <div className='flex flex-col basis-45/100 min-h-0 gap-3'>
            <div className='flex items-center gap-2 text-p3 text-gray-500'>
              <Icon name='InfoCircle' size={16} color='info' />
              <span className='text-info'>주식 가격은 09:00와 13:00마다 변경됩니다.</span>
            </div>
            <div className='flex-1 min-h-0'>
              <SpecifiedStockGraph stock={selectedStock} onBack={() => setSelectedStock(null)} />
            </div>
          </div>
        ) : (
          <>
            <div className='flex items-center gap-2 text-p3 text-gray-500'>
              <Icon name='InfoCircle' size={16} color='info' />
              <span className='text-info'>주식 가격은 09:00와 13:00마다 변경됩니다.</span>
            </div>

            <Card
              titleLeft={showMyStockChart ? '나의 투자 현황' : '실시간 차트'}
              titleRight={
                <div className='flex items-center gap-2'>
                  <div className='text-c1 text-grey'>2025.03.31 13:00 기준</div>
                  <Button
                    variant='solid'
                    size='sm'
                    color='primary'
                    onClick={() => setShowMyStockChart((prev) => !prev)}
                  >
                    {showMyStockChart ? '실시간 차트' : '나의 투자 현황'}
                  </Button>
                </div>
              }
              className='flex flex-col basis-45/100 min-h-0'
            >
              <div className='flex-1 overflow-auto [&::-webkit-scrollbar]:hidden'>
                {showMyStockChart ? (
                  <MyStockChart />
                ) : (
                  <TableView columns={columns} rows={rows} onRowClick={handleRowClick} />
                )}
              </div>
            </Card>
          </>
        )}

        {/* 오늘의 뉴스는 항상 노출 */}
        <Card
          titleLeft='오늘의 뉴스'
          titleRight={
            <div className='flex items-center gap-2'>
              <span className='text-c1 text-grey'>
                모든 투자 결정과 그 결과에 대한 책임은 전적으로 투자자 본인에게 있습니다.
              </span>
            </div>
          }
          titleSize='l'
          className='flex flex-col basis-55/100 min-h-0'
        >
          <div className='flex-1 overflow-auto [&::-webkit-scrollbar]:hidden'>
            <TodayNews />
          </div>
        </Card>
      </div>
    </SidebarLayout>
  );
};

export default StudentStock;
