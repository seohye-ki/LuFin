import DashboardCard from './DashboardCard';
import LufinCoin from '../../../../assets/svgs/lufin-coin-24.svg';
import { Link } from 'react-router-dom';
import { paths } from '../../../../routes/paths';

interface MissionInfo {
  name: string;
  reward: number;
  daysLeft?: number;
}

interface MissionSectionProps {
  completedCount: number;
  currentMission?: MissionInfo;
  totalReward: number;
}

const MissionSection = ({ completedCount, currentMission, totalReward }: MissionSectionProps) => {
  return (
    <DashboardCard
      titleLeft='미션'
      // titleRight={<button className="text-c2 text-grey hover:text-dark-grey">자세히 보기</button>}
      className='flex-1'
    >
      <div className='flex flex-col gap-4'>
        <div className='h-24 flex flex-row items-center justify-center gap-2 p-3 bg-broken-white rounded-lg'>
          {currentMission ? (
            <div className='flex flex-col gap-2'>
              <div className='flex items-center justify-between'>
                <span className='text-h3 font-medium'>{currentMission.name}</span>
                <div className='flex items-center gap-1 bg-light-cyan px-3 py-1 rounded-full'>
                  <img src={LufinCoin} alt='루핀' className='w-5 h-5' />
                  <span className='text-p2 font-semibold text-black'>
                    {currentMission.reward.toLocaleString()}
                  </span>
                </div>
              </div>
              {currentMission.daysLeft !== undefined && (
                <span className='text-p2 text-grey'>{currentMission.daysLeft}일 남음</span>
              )}
            </div>
          ) : (
            <div className='flex flex-col gap-2 items-center'>
              <span className='text-p2 text-grey'>진행중인 미션이 없어요.</span>
              <Link to={paths.MISSION} className='text-c1 text-info hover:underline'>
                미션 확인하러 가기
              </Link>
            </div>
          )}
        </div>

        <div className='h-16 flex gap-4'>
          <div className='flex-1 p-3 bg-broken-white rounded-lg'>
            <div className='flex items-center justify-between'>
              <span className='text-p2 text-grey'>총 미션 완료</span>
              <div className='flex items-center gap-1'>
                <span className='text-h2 font-bold'>{completedCount}</span>
                <span className='text-p2 text-grey'>회</span>
              </div>
            </div>
          </div>

          <div className='h-16 flex-1 p-3 bg-broken-white rounded-lg'>
            <div className='flex items-center justify-between'>
              <span className='text-p2 text-grey'>보상 합계</span>
              <div className='flex items-center gap-2'>
                <img src={LufinCoin} alt='루핀' className='w-5 h-5' />
                <span className='text-h2 font-bold'>{totalReward.toLocaleString()}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </DashboardCard>
  );
};

export default MissionSection;
