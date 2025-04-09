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
  // 순위별 스타일 결정
  const getRankStyle = (ranking: number) => {
    switch (ranking) {
      case 1:
        return {
          containerScale: 'scale-125',
          imageSize: 'w-[48px] h-[48px]',
          badgeStyle: 'bg-gradient-to-br from-yellow to-warning ring-2 ring-yellow',
          badgeSize: 'h-7 w-7',
        };
      case 2:
        return {
          containerScale: 'scale-115',
          imageSize: 'w-[45px] h-[45px]',
          badgeStyle: 'bg-gradient-to-br from-light-grey to-grey ring-2 ring-light-grey',
          badgeSize: 'h-6 w-6',
        };
      case 3:
        return {
          containerScale: 'scale-110',
          imageSize: 'w-[42px] h-[42px]',
          badgeStyle: 'bg-gradient-to-br from-warning-light to-warning-dark ring-2 ring-warning-light',
          badgeSize: 'h-6 w-6',
        };
      default:
        return {
          containerScale: '',
          imageSize: 'w-[42px] h-[42px]',
          badgeStyle: 'bg-grey',
          badgeSize: 'h-6 w-6',
        };
    }
  };

  const rankStyle = getRankStyle(student.ranking);

  return (
    <div
      className={`flex flex-col items-center ${
        isCurrentUser ? 'scale-110 transform duration-200' : ''
      } ${rankStyle.containerScale} transform duration-200`}
    >
      <div className={`flex flex-col items-center ${
        isCurrentUser ? 'p-2 rounded-xl ring-2 ring-light-cyan bg-light-cyan/10' : ''
      }`}>
        {/* Profile Image with Ranking Badge */}
        <div className={`relative ${rankStyle.imageSize} mb-1`}>
          <img
            src={profileImage}
            alt={`${student.name} 프로필 이미지`}
            className="rounded-full w-full h-full"
          />
          {/* Ranking Badge */}
          <div
            className={`absolute -right-3 -top-3 z-10 flex items-center justify-center rounded-full text-xs font-bold text-white ${rankStyle.badgeStyle} ${rankStyle.badgeSize}`}
          >
            {student.ranking}
          </div>
        </div>
        {/* Name */}
        <p className={`text-c1 ${isCurrentUser ? 'font-bold' : ''}`}>{student.name}</p>
      </div>
    </div>
  );
};

export default StudentRankCard;
