import DashboardCard from './DashboardCard';
import StudentRankCard from './StudentRankCard';

interface RankingData {
  memberId: number;
  name: string;
  asset: number;
  rank: number;
  profileImage: string;
}

interface ClassAssetRankingProps {
  rankings: RankingData[];
  myMemberId: number;
}

const ClassAssetRanking = ({ rankings, myMemberId }: ClassAssetRankingProps) => {
  return (
    <DashboardCard titleLeft='우리반 자산 랭킹' className='flex-1'>
      <div className='flex min-w-full justify-center px-4 h-fit'>
        <div className='flex flex-wrap justify-center gap-8'>
          {rankings.map((item) => (
            <StudentRankCard
              key={item.memberId}
              student={{
                name: item.name,
                ranking: item.rank,
              }}
              isCurrentUser={item.memberId === myMemberId}
              profileImage={item.profileImage}
            />
          ))}
        </div>
      </div>
    </DashboardCard>
  );
};

export default ClassAssetRanking;
