import DashboardCard from './DashboardCard';
import CreditChart from '../../../../components/Graph/CreditChart';

interface CreditScoreFrontProps {
  userName: string;
  creditScore: number;
  creditRating: string;
  onFlip: () => void;
}

const CreditScoreFront = ({
  userName,
  creditScore,
  creditRating,
  onFlip,
}: CreditScoreFrontProps) => (
  <DashboardCard
    titleLeft='신용점수'
    titleRight={
      <button className='text-p2 px-1 text-grey hover:text-dark-grey' onClick={onFlip}>
        자세히 보기
      </button>
    }
    className='flex-1 w-full absolute backface-hidden p-4'
  >
    <div className='w-full h-full flex items-center justify-center'>
      <div className='flex flex-col items-center w-full max-w-full'>
        <div className='relative w-full max-w-[min(100%,280px)] mx-auto'>
          <CreditChart creditRating={creditRating} />
        </div>

        <div className='mt-4 bg-broken-white px-4 py-2 rounded-full w-fit max-w-full'>
          <div className='text-p2 text-grey text-center flex items-center gap-1 flex-wrap justify-center'>
            <span className='text-black font-medium'>{userName}</span>님의 신용점수는 총
            <div className='inline-flex items-center gap-1'>
              <span className='text-p2 text-info font-bold'>{creditScore}</span>
              <span>점</span>
            </div>
            이에요.
          </div>
        </div>
      </div>
    </div>
  </DashboardCard>
);

export default CreditScoreFront;
