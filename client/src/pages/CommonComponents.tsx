import Profile from '../components/Profile/Profile';
import Lufin from '../components/Lufin/Lufin';

const CommonComponents = () => {
  return (
    <div className='p-8 min-h-screen'>
      <h1 className='text-h1 font-regular text-black mb-4'>Common Components</h1>
      <div className='flex flex-col gap-4'>
        <Profile name='이재현' profileImage='https://picsum.photos/200/300?random=1' />
        <Profile
          name='이재현 선생님'
          profileImage='https://picsum.photos/200/300?random=1'
          variant='certification'
          certificationNumber='5학년 1반'
        />
        <div className='flex flex-col gap-12'>
          <Lufin count={15200} size={16} />
          <Lufin count={15200} size={24} />
          <Lufin count={15200} size={32} />
          <Lufin count={15200} size={40} />
          <Lufin count={15200} size={52} />
        </div>
      </div>
    </div>
  );
};

export default CommonComponents;
