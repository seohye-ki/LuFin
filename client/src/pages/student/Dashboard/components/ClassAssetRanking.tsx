import Card from '../../../../components/Card/Card';
import StudentRankCard, { StudentRanking } from './StudentRankCard';

const ClassAssetRanking = () => {
  const getRandomProfileImage = () => {
    const seed = Math.floor(Math.random() * 1000);
    return `https://api.dicebear.com/9.x/bottts-neutral/svg?seed=${seed}`;
  };

  const studentRankings: StudentRanking[] = [
    { name: '이재현', ranking: 1 },
    { name: '조홍균', ranking: 2 },
    { name: '최민주', ranking: 3 },
    { name: '신유정', ranking: 4 },
    { name: '양은석', ranking: 5 },
    { name: '김서현', ranking: 6 },
    { name: '박하준', ranking: 7 },
    { name: '김은진', ranking: 8 },
    { name: '윤이솔', ranking: 9 },
    { name: '이인범', ranking: 10 },
  ];

  const currentUser = '최민주';

  return (
    <Card titleLeft='우리반 자산 랭킹' className='flex-1'>
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
    </Card>
  );
};

export default ClassAssetRanking;
