import Profile from '../../../../components/Profile/Profile';

export type StudentRanking = {
  name: string;
  ranking: number;
};

interface StudentRankCardProps {
  student: StudentRanking;
  isCurrentUser: boolean;
  profileImage: string;
}

const StudentRankCard = ({ student, isCurrentUser, profileImage }: StudentRankCardProps) => {
  return (
    <div
      className={`relative flex flex-col items-center ${
        isCurrentUser ? 'scale-110 transform duration-200' : ''
      }`}
    >
      {/* Ranking Badge */}
      <div
        className={`absolute -right-3 -top-3 z-10 flex h-6 w-6 items-center justify-center rounded-full text-xs font-bold text-white ${
          student.ranking <= 3 ? 'bg-gradient-to-br from-yellow to-warning' : 'bg-grey'
        }`}
      >
        {student.ranking}
      </div>

      {/* Profile Section */}
      <div
        className={`mb-3 ${
          isCurrentUser ? 'rounded-full ring-10 ring-light-cyan bg-light-cyan ring-offset-0' : ''
        }`}
      >
        <Profile
          name={student.name}
          profileImage={profileImage}
          variant='column'
          className='h-auto'
        />
      </div>
    </div>
  );
};

export default StudentRankCard;
