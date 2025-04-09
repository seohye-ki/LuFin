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
      className={`flex flex-col items-center ${
        isCurrentUser ? 'scale-110 transform duration-200' : ''
      }`}
    >
      <div className={`flex flex-col items-center ${
        isCurrentUser ? 'p-2 rounded-xl ring-2 ring-light-cyan bg-light-cyan/10' : ''
      }`}>
        {/* Profile Image with Ranking Badge */}
        <div className="relative w-[42px] h-[42px] mb-1">
          <img
            src={profileImage}
            alt={`${student.name} 프로필 이미지`}
            className="rounded-full w-full h-full"
          />
          {/* Ranking Badge */}
          <div
            className={`absolute -right-3 -top-3 z-10 flex h-6 w-6 items-center justify-center rounded-full text-xs font-bold text-white ${
              student.ranking <= 3 ? 'bg-gradient-to-br from-yellow to-warning' : 'bg-grey'
            }`}
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
