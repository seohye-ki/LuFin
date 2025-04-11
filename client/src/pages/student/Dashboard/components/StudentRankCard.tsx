export type StudentRanking = {
  name: string;
  ranking: number;
  asset?: number;
};

interface StudentRankCardProps {
  student: StudentRanking;
  isCurrentUser: boolean;
  profileImage: string;
}

const StudentRankCard = ({ student, isCurrentUser, profileImage }: StudentRankCardProps) => {
  const getBadgeStyle = (ranking: number) => {
    if (ranking === 1) return 'bg-gradient-to-br from-yellow-400 to-yellow-600';
    if (ranking === 2) return 'bg-gradient-to-br from-gray-300 to-gray-500';
    if (ranking === 3) return 'bg-gradient-to-br from-amber-600 to-amber-800';
    return 'bg-grey';
  };

  return (
    <div
      className={`flex items-center gap-3 ${
        isCurrentUser ? 'scale-105 transform duration-200' : ''
      }`}
    >
      <div
        className={`flex items-center gap-3 ${
          isCurrentUser ? 'p-2 rounded-xl ring-2 ring-light-cyan bg-light-cyan/10 w-full' : ''
        }`}
      >
        {/* Profile Image with Ranking Badge */}
        <div className='relative w-[42px] h-[42px]'>
          <img
            src={profileImage}
            alt={`${student.name} 프로필 이미지`}
            className='rounded-full w-full h-full object-cover'
          />
          {/* Ranking Badge */}
          <div
            className={`absolute -right-2 -top-2 z-10 flex h-6 w-6 items-center justify-center rounded-full text-xs font-bold text-white ${getBadgeStyle(
              student.ranking,
            )}`}
          >
            {student.ranking}
          </div>
        </div>
        {/* Name and Asset */}
        <div className='flex flex-col'>
          <p className={`text-c1 ${isCurrentUser ? 'font-bold' : ''}`}>{student.name}</p>
          {student.asset !== undefined && (
            <p className='text-xs text-gray-500'>{student.asset.toLocaleString()}루핀</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default StudentRankCard;
