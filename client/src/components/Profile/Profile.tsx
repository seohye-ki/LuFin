import { twMerge } from 'tailwind-merge';

interface ProfileProps {
  name: string;
  profileImage: string;
  certificationNumber?: string;
  direction?: 'row' | 'column';
  textDirection?: 'row' | 'column';
}

const Profile = ({
  name,
  profileImage,
  certificationNumber,
  direction = 'row',
  textDirection = 'column',
}: ProfileProps) => {
  return (
    <div
      className={twMerge(
        'w-full h-[58px] flex border border-purple rounded-lg p-2 gap-2 justify-center items-center',
        direction === 'row' ? 'flex-row' : 'flex-col',
      )}
    >
      <img
        src={profileImage}
        alt={`${name} 프로필 이미지`}
        className='w-[42px] h-[42px] rounded-full'
      />
      <div
        className={twMerge(
          'flex justify-center items-center gap-1',
          textDirection === 'row' ? 'flex-row' : 'flex-col',
        )}
      >
        <p className='text-p2-medium'>{name}</p>
        {certificationNumber && <p className='text-c1'>{certificationNumber}</p>}
      </div>
    </div>
  );
};

export default Profile;
