import { useState } from 'react';
import CreditScoreFront from './CreditScoreFront';
import CreditScoreBack, { CreditActivity } from './CreditScoreBack';

interface CreditScoreCardProps {
  userName: string;
  creditScore: number;
  creditRating: string;
  recentActivities?: CreditActivity[];
}

const CreditScoreCard = ({
  userName = '최민주',
  creditScore = 89,
  creditRating = 'A+',
  recentActivities = [
    { type: 'increase', amount: 2, description: '미션성공', date: '3월 19일 14:23' },
    { type: 'increase', amount: 2, description: '미션성공', date: '3월 18일 12:02' },
    { type: 'decrease', amount: 2, description: '미션실패', date: '3월 18일 12:02' },
    { type: 'decrease', amount: 2, description: '미션실패', date: '3월 18일 12:02' },
    { type: 'decrease', amount: 2, description: '미션실패', date: '3월 18일 12:02' },
    { type: 'decrease', amount: 2, description: '미션실패', date: '3월 18일 12:02' },
    { type: 'decrease', amount: 2, description: '미션실패', date: '3월 18일 12:02' },
  ],
}: CreditScoreCardProps) => {
  const [isFlipped, setIsFlipped] = useState(false);

  const handleFlip = () => {
    setIsFlipped(!isFlipped);
  };

  return (
    <div className='relative w-full h-full perspective-1000'>
      <div
        className={`w-full h-full transition-transform duration-700 transform-style-preserve-3d ${
          isFlipped ? 'rotate-y-180' : ''
        }`}
      >
        <CreditScoreFront
          userName={userName}
          creditScore={creditScore}
          creditRating={creditRating}
          onFlip={handleFlip}
        />
        <CreditScoreBack recentActivities={recentActivities} onFlip={handleFlip} />
      </div>
    </div>
  );
};

export default CreditScoreCard;
