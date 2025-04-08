import DashboardCard from './DashboardCard';
import StudentRankCard, { StudentRanking } from './StudentRankCard';

interface RankingData {
  memberId: number;
  name: string;
  asset: number;
  rank: number;
}

interface ClassAssetRankingProps {
  rankings: RankingData[];
}

const ClassAssetRanking = ({ rankings }: ClassAssetRankingProps) => {
  const getRandomProfileImage = () => {
    const seed = Math.floor(Math.random() * 1000);
    return `https://api.dicebear.com/9.x/bottts-neutral/svg?seed=${seed}`;
  };

  // Find current user (assuming current user is the one with the highest asset)
  const currentUser = rankings.length > 0 ? rankings[0].name : '';

  // Convert API rankings to the format required by StudentRankCard
  const studentRankings: StudentRanking[] = rankings.map((item) => ({
    name: item.name,
    ranking: item.rank,
  }));

  return (
    <DashboardCard titleLeft='우리반 자산 랭킹' className='flex-1'>
      <div className='flex min-w-full justify-center px-4 h-fit'>
        <div className='flex flex-wrap justify-center gap-8'>
          {studentRankings.map((student) => (
            <StudentRankCard
              key={student.name}
              student={student}
              isCurrentUser={student.name === currentUser}
              profileImage={getRandomProfileImage()}
            />
          ))}
        </div>
      </div>
    </DashboardCard>
  );
};

export default ClassAssetRanking;
