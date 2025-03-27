import { twMerge } from 'tailwind-merge';

interface ProfileProps {
  name: string;
  profileImage: string;
  certificationNumber?: string;
  variant?: 'column' | 'row';
  className?: string;
}

const Profile = ({
  name,
  profileImage,
  certificationNumber,
  variant = 'column',
  className,
}: ProfileProps) => {
  const isColumn = variant === 'column';

  return (
    <div
      className={twMerge(
        'flex',
        isColumn
          ? 'flex-col p-0 gap-[4px] items-center w-full h-[58px]'
          : 'rounded-lg flex-row px-2 py-2 gap-[10px] items-center w-full h-[58px]',
        className,
      )}
    >
      <img
        src={profileImage}
        alt={`${name} 프로필 이미지`}
        className={twMerge('rounded-full w-[42px] h-[42px]')}
      />
      <div
        className={twMerge('flex', isColumn ? 'flex-col items-center' : 'flex-col justify-center')}
      >
        <p className={isColumn ? 'text-c1' : 'text-p2 font-medium'}>{name}</p>
        {!isColumn && certificationNumber && (
          <p className='text-c1 font-regular'>{certificationNumber}</p>
        )}
      </div>
    </div>
  );
};

export default Profile;
