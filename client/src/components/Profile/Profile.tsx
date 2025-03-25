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
          ? 'flex-col p-0 gap-[4px] items-center w-[42px] h-[58px]'
          : 'border border-purple rounded-lg flex-row px-4 py-2 gap-[10px] items-center w-[198px] h-[58px]',
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
        <p className={isDefault ? 'text-c2' : 'text-p2-medium'}>{name}</p>
        {!isDefault && certificationNumber && <p className='text-c1'>{certificationNumber}</p>}
      </div>
    </div>
  );
};

export default Profile;
