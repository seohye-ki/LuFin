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
  // 랭킹 정렬
  const sortedRankings = [...rankings].sort((a, b) => a.rank - b.rank);

  return (
    <DashboardCard titleLeft='우리반 자산 랭킹' className='flex-1 h-full'>
      <div className='flex flex-col h-full justify-start px-4 py-2 overflow-y-auto'>
        <div className='flex flex-col gap-4'>
          {sortedRankings.map((item) => (
            <StudentRankCard
              key={item.memberId}
              student={{
                name: item.name,
                ranking: item.rank,
                asset: item.asset,
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
