import Card from '../../../../components/Card/Card';
import LufinCoin from '../../../../assets/svgs/lufin-coin-24.svg';

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
    <Card
      titleLeft='미션'
      // titleRight={<button className="text-c2 text-grey hover:text-dark-grey">자세히 보기</button>}
      className='flex-1'
    >
      <div className='flex flex-col gap-4'>
        <div className='flex flex-col gap-2 p-3 bg-broken-white rounded-lg'>
          <div className='flex items-center justify-between'>
            <span className='text-p2 text-grey'>진행중인 미션</span>
          </div>
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
              {currentMission.daysLeft && (
                <span className='text-p2 text-grey'>{currentMission.daysLeft}일 남음</span>
              )}
            </div>
          ) : (
            <span className='text-p2 text-grey'>진행중인 미션이 없습니다.</span>
          )}
        </div>

        <div className='flex gap-4'>
          <div className='flex-1 p-3 bg-broken-white rounded-lg'>
            <div className='flex items-center justify-between'>
              <span className='text-p2 text-grey'>총 미션 완료</span>
              <div className='flex items-center gap-1'>
                <span className='text-h2 font-bold'>{completedCount}</span>
                <span className='text-p2 text-grey'>개</span>
              </div>
            </div>
          </div>

          <div className='flex-1 p-3 bg-broken-white rounded-lg'>
            <div className='flex items-center justify-between'>
              <span className='text-p2 text-grey'>총 리워드</span>
              <div className='flex items-center gap-2'>
                <img src={LufinCoin} alt='루핀' className='w-5 h-5' />
                <span className='text-h2 font-bold'>{totalReward.toLocaleString()}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Card>
  );
};

export default MissionSection;
