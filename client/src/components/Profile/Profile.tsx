import { twMerge } from 'tailwind-merge';

interface ProfileProps {
  name: string;
  profileImage: string;
  certificationNumber?: string;
  variant?: 'default' | 'certification';
}

const Profile = ({
  name,
  profileImage,
  certificationNumber,
  variant = 'default',
}: ProfileProps) => {
  const isDefault = variant === 'default';

  return (
    <div
      className={twMerge(
        'flex',
        isDefault
          ? 'flex-col p-0 gap-[4px] items-center w-full h-[58px]'
          : 'border border-purple-30 rounded-lg flex-row px-2 py-2 gap-[10px] items-center w-full h-[58px]',
      )}
    >
      <img
        src={profileImage}
        alt={`${name} 프로필 이미지`}
        className={twMerge('rounded-full w-[42px] h-[42px]')}
      />
      <div
        className={twMerge('flex', isDefault ? 'flex-col items-center' : 'flex-col justify-center')}
      >
        <p className={isDefault ? 'text-c2' : 'text-p2 font-medium'}>{name}</p>
        {!isDefault && certificationNumber && (
          <p className='text-c1 font-regular'>{certificationNumber}</p>
        )}
      </div>
    </div>
  );
};

export default Profile;
