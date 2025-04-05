import { Icon } from '../../../../components/Icon/Icon';

const StockInfoNotice = () => {
  return (
    <div className='flex items-center gap-2 text-p3 text-info'>
      <Icon name='InfoCircle' size={16} />
      <span>주식 가격은 09:00와 13:00마다 변경됩니다.</span>
    </div>
  );
};

export default StockInfoNotice;
