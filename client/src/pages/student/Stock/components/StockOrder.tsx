import { useState } from 'react';
import Button from '../../../../components/Button/Button';
import Lufin from '../../../../components/Lufin/Lufin';
import { StockProduct } from '../../../../types/Stock/stock';

interface StockOrderProps {
  stock: StockProduct;
}

const StockOrder = ({ stock }: StockOrderProps) => {
  const [tab, setTab] = useState<'buy' | 'sell'>('buy');
  const [quantity, setQuantity] = useState(1);

  // 예시로 최대 보유 수량 또는 예산 기준 설정
  const MAX_QUANTITY = 10;

  const handleQuantityChange = (delta: number) => {
    setQuantity((prev) => Math.max(1, prev + delta));
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(e.target.value, 10);
    if (!isNaN(value) && value > 0) {
      setQuantity(value);
    }
  };

  const handlePercentClick = (percent: number) => {
    const calculated = Math.max(1, Math.floor(MAX_QUANTITY * percent));
    setQuantity(calculated);
  };

  const total = quantity * stock.currentPrice;

  return (
    <div className='flex flex-col gap-4 w-full p-4'>
      {/* 탭 */}
      <div className='flex rounded-full bg-gray-100'>
        <button
          className={`flex-1 py-2 rounded-full ${
            tab === 'buy' ? 'bg-light-cyan text-black font-semibold' : 'text-gray-400'
          }`}
          onClick={() => setTab('buy')}
        >
          구매하기
        </button>
        <button
          className={`flex-1 py-2 rounded-full ${
            tab === 'sell' ? 'bg-light-cyan text-black font-semibold' : 'text-gray-400'
          }`}
          onClick={() => setTab('sell')}
        >
          판매하기
        </button>
      </div>

      {/* 현재 가격 */}
      <div className='flex justify-between items-center'>
        <span className='text-gray-500'>현재 가격</span>
        <Lufin size='s' count={stock.currentPrice} />
      </div>

      {/* 수량 조절 */}
      <div className='flex justify-between items-center gap-2'>
        <span className='text-gray-500'>수량</span>
        <div className='flex items-center gap-2'>
          <input
            type='number'
            value={quantity}
            onChange={handleInputChange}
            className='w-full text-center border border-gray-300 rounded-md py-1'
            min={1}
          />
          <button
            onClick={() => handleQuantityChange(-1)}
            className='w-7 h-7 rounded-md bg-gray-100 text-xl'
          >
            -
          </button>
          <button
            onClick={() => handleQuantityChange(1)}
            className='w-7 h-7 rounded-md bg-gray-100 text-xl'
          >
            +
          </button>
        </div>
      </div>

      {/* 퍼센트 버튼 */}
      <div className='flex justify-between gap-2'>
        {[0.1, 0.25, 0.5, 1].map((p, i) => (
          <button
            key={i}
            onClick={() => handlePercentClick(p)}
            className='flex-1 rounded-lg border border-gray-300 py-1'
          >
            {p === 1 ? '최대' : `${p * 100}%`}
          </button>
        ))}
      </div>

      {/* 총 금액 or 수익 */}
      <div className='flex justify-between items-center'>
        <span className='text-gray-500'>{tab === 'buy' ? '총 주문 금액' : '총 예상 수익'}</span>
        <Lufin size='s' count={total} />
      </div>

      {/* 주문 버튼 */}
      <Button
        variant='solid'
        size='md'
        color={tab === 'buy' ? 'danger' : 'info'}
        className='w-full mt-2'
        onClick={() => {
          const type = tab === 'buy' ? '매수' : '매도';
          console.log(`${type} ${quantity}주 (${total} 루핀)`);
        }}
      >
        {tab === 'buy' ? '구매하기' : '판매하기'}
      </Button>
    </div>
  );
};

export default StockOrder;
