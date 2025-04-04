import { useState } from 'react';
import Button from '../../../../components/Button/Button';
import Lufin from '../../../../components/Lufin/Lufin';
import { Icon } from '../../../../components/Icon/Icon';
import { StockProduct } from '../../../../types/stock/stock';
import useAlertStore from '../../../../libs/store/alertStore';

interface StockOrderProps {
  stock: StockProduct;
}

const StockOrder = ({ stock }: StockOrderProps) => {
  const [tab, setTab] = useState<'buy' | 'sell'>('buy');
  const [quantity, setQuantity] = useState(1);

  // 예시로 최대 보유 수량
  const MAX_QUANTITY = 10;

  const handleQuantityChange = (delta: number) => {
    setQuantity((prev) => {
      const newQuantity = prev + delta;
      return Math.max(1, Math.min(newQuantity, MAX_QUANTITY));
    });
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(e.target.value, 10);
    if (!isNaN(value) && value > 0) {
      setQuantity(Math.max(1, Math.min(value, MAX_QUANTITY)));
    }
  };

  const handlePercentClick = (percent: number) => {
    const calculated = Math.max(1, Math.floor(MAX_QUANTITY * percent));
    setQuantity(calculated);
  };

  const total = quantity * stock.currentPrice;

  return (
    <div className='flex flex-col gap-4 w-full h-full p-4'>
      {/* 탭 */}
      <div className='flex rounded-full bg-gray-100 '>
        <button
          className={`flex-1 py-2 rounded-full text-p3 ${
            tab === 'buy' ? 'bg-light-cyan text-black font-semibold' : 'text-gray-400'
          }`}
          onClick={() => setTab('buy')}
        >
          구매하기
        </button>
        <button
          className={`flex-1 py-2 rounded-full text-p3 ${
            tab === 'sell' ? 'bg-light-cyan text-black font-semibold' : 'text-gray-400'
          }`}
          onClick={() => setTab('sell')}
        >
          판매하기
        </button>
      </div>
      <div className='flex flex-col gap-4  overflow-auto [&::-webkit-scrollbar]:hidden px-4'>
        {/* 현재 가격 */}
        <div className='flex justify-between items-center'>
          <span className='text-p3 text-grey'>현재 가격</span>
          <Lufin size='s' count={stock.currentPrice} />
        </div>

        {/* 수량 조절 */}
        <div className='flex justify-between items-center gap-2'>
          <span className='text-p3 text-grey'>수량</span>
          <div className='w-[49%] relative'>
            <input
              value={quantity}
              onChange={handleInputChange}
              className='w-full text-center border border-gray-300 rounded-lg py-1'
              min={1}
              max={MAX_QUANTITY}
            />
            <button
              onClick={() => handleQuantityChange(-1)}
              className='absolute left-0 top-1/2 -translate-y-1/2 px-1 py-1 text-lg text-gray-600'
            >
              <Icon name='Minus' size={18} />
            </button>
            <button
              onClick={() => handleQuantityChange(1)}
              className='absolute right-0 top-1/2 -translate-y-1/2 px-1 py-1 text-lg text-gray-600'
            >
              <Icon name='Add' size={18} />
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
          <span className='text-p3 text-grey'>
            {tab === 'buy' ? '총 주문 금액' : '총 예상 수익'}
          </span>
          <Lufin size='s' count={total} />
        </div>
      </div>
      {/* 주문 버튼 */}
      <Button
        variant='solid'
        size='md'
        color={tab === 'buy' ? 'danger' : 'info'}
        className='w-full'
        onClick={() => {
          const type = tab === 'buy' ? '구매' : '판매';

          useAlertStore.getState().showAlert(
            `${stock.name} ${type} ${quantity}주`,
            <div className='flex justify-center items-center gap-2'>
              <span className='text-p3 text-grey'>현재 가격</span>
              <Lufin size='s' count={stock.currentPrice} />
            </div>,
            '',
            'info',
            {
              label: '취소',
              onClick: () => useAlertStore.getState().hideAlert(),
              variant: 'solid',
              color: 'neutral',
            },
            {
              label: `${type}하기`,
              onClick: () => {
                useAlertStore.getState().hideAlert();
              },
              variant: 'solid',
              color: tab === 'buy' ? 'danger' : 'info',
            },
          );
        }}
      >
        {tab === 'buy' ? '구매하기' : '판매하기'}
      </Button>
    </div>
  );
};

export default StockOrder;
