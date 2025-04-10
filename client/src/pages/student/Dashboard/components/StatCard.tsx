import DashboardCard from './DashboardCard';
import LufinCoin from '../../../../assets/svgs/lufin-coin-24.svg';
import { Icon } from '../../../../components/Icon/Icon';

interface StatCardProps {
  title: string;
  amount: number;
  trend?: {
    value: number;
    isPositive: boolean;
  };
}

const getIconConfig = (title: string) => {
  if (title.includes('소비')) return { name: 'ShoppingBag', bg: 'bg-info' };
  if (title.includes('투자')) return { name: 'PresentionChart', bg: 'bg-warning' };
  if (title.includes('대출')) return { name: 'MoneyRecive', bg: 'bg-danger' };
  return { name: 'InfoCircle', bg: 'bg-grey' };
};

const getCompareText = (title: string) => (title.includes('투자') ? '어제' : '지난주');

const renderTrendText = (title: string, amount: number, trend?: StatCardProps['trend']) => {
  const compareTarget = getCompareText(title);

  if (title.includes('대출') && trend === undefined) {
    const message = amount === 0 ? '대출이 없어요.' : '이자가 연체되지 않도록 주의하세요.';
    return <span className='text-p2 text-grey'>{message}</span>;
  }

  if (!trend) {
    return <span className='text-p2 text-transparent'>-</span>;
  }

  if (trend.value === 0) {
    return <span className='text-p2 text-grey'>{compareTarget}와 동일해요.</span>;
  }

  const direction = trend.isPositive ? '올랐어요' : '내렸어요';
  const color = trend.isPositive ? 'text-success' : 'text-danger';

  return (
    <span className={`text-p2 ${color}`}>
      {compareTarget}보다 {Math.abs(trend.value)}% {direction}
    </span>
  );
};

const StatCard = ({ title, amount, trend }: StatCardProps) => {
  const { name, bg } = getIconConfig(title);

  return (
    <DashboardCard className='flex-1'>
      <div className='flex flex-col justify-between h-full'>
        <div className='flex flex-row items-center gap-2.5'>
          <div className={`${bg} w-fit rounded-xl p-1.5`}>
            <Icon name={name as any} color='white' variant='Bold' size={24} />
          </div>
          <span className='text-h3 font-bold text-black'>{title}</span>
        </div>

        <div className='flex flex-col gap-0.5'>
          <div className='flex items-center gap-2'>
            <img src={LufinCoin} alt='루핀' className='w-5 h-5' />
            <span className='text-h2 font-bold'>{amount.toLocaleString()}</span>
          </div>
        </div>

        <div className='font-medium'>{renderTrendText(title, amount, trend)}</div>
      </div>
    </DashboardCard>
  );
};

export default StatCard;
