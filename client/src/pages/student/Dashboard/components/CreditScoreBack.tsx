import DashboardCard from './DashboardCard';
import { Icon } from '../../../../components/Icon/Icon';

export interface CreditActivity {
  type: 'increase' | 'decrease';
  amount: number;
  description: string;
  date: string;
}

interface CreditScoreBackProps {
  recentActivities: CreditActivity[];
  onFlip: () => void;
}

const CreditScoreBack = ({ recentActivities = [], onFlip }: CreditScoreBackProps) => (
  <DashboardCard
    titleLeft='신용도 변경 기록'
    titleRight={
      <button className='text-p2 px-1 text-grey hover:text-dark-grey' onClick={onFlip}>
        돌아가기
      </button>
    }
    className='flex-1 h-full overflow-hidden absolute w-full backface-hidden rotate-y-180'
  >
    <div className='h-full flex flex-col'>
      <div className='flex-1 min-h-0 max-h-[calc(100%-3rem)] overflow-y-auto scrollbar-hide'>
        <div className='flex flex-col gap-2 p-4'>
          {recentActivities.map((activity, index) => (
            <div
              key={index}
              className='flex items-center justify-between p-3 bg-broken-white rounded-lg'
            >
              <div className='flex items-center gap-2'>
                <Icon
                  name={activity.type === 'increase' ? 'ArrowCircleUp' : 'ArrowCircleDown'}
                  size={20}
                  color={activity.type === 'increase' ? 'success' : 'danger'}
                />
                <span className='text-p1 font-medium'>{activity.description}</span>
              </div>
              <div className='flex items-center gap-4'>
                <span
                  className={`text-p2 font-semibold ${
                    activity.type === 'increase' ? 'text-success' : 'text-danger'
                  }`}
                >
                  {activity.type === 'increase' ? '+' : '-'}
                  {activity.amount}
                </span>
                <span className='text-p2 text-grey'>{activity.date}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  </DashboardCard>
);

export default CreditScoreBack;
