import DashboardCard from './DashboardCard';
import LufinCoin from '../../../../assets/svgs/lufin-coin-24.svg';

interface StatCardProps {
  title: string;
  amount: number;
  trend?: {
    value: number;
    isPositive: boolean;
  };
}

const StatCard = ({ title, amount, trend }: StatCardProps) => {
  return (
    <DashboardCard className='flex-1 h-fit'>
      <div className='h-full flex items-center justify-between'>
        <div className='flex flex-col gap-1 w-full px-3'>
          <div className='flex items-center justify-between'>
            <span className='text-p1 font-semibold text-black'>{title}</span>
            {trend && (
              <span className={`text-p2 ${trend.isPositive ? 'text-success' : 'text-danger'}`}>
                {trend.isPositive ? '↑' : '↓'} {Math.abs(trend.value)}%
              </span>
            )}
          </div>
          <div className='flex items-end gap-2'>
            <img src={LufinCoin} alt='루핀' className='mb-1 w-6 h-6' />
            <span className='text-h2 font-bold'>{amount.toLocaleString()}</span>
          </div>
        </div>
      </div>
    </DashboardCard>
  );
};

export default StatCard;
