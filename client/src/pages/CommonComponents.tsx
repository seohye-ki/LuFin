import Profile from '../components/Profile/Profile';
import Lufin from '../components/Lufin/Lufin';
import Sidebar from '../components/Sidebar/Sidebar';

const CommonComponents = () => {
  return (
    <div className='p-8 bg-broken-white'>
      <h1 className='text-h1 font-bold text-black mb-4'>Common Components</h1>
      <div className='flex gap-8'>
        <div className='flex flex-col gap-4'>
          <Profile name='이재현' profileImage='https://picsum.photos/200/300?random=1' />
          <Profile
            name='이재현 선생님'
            profileImage='https://picsum.photos/200/300?random=1'
            variant='certification'
            certificationNumber='5학년 1반 12번'
          />
          <div className='flex flex-col gap-12'>
            <Lufin count={15200} size={16} />
            <Lufin count={15200} size={24} />
            <Lufin count={15200} size={28} />
            <Lufin count={15200} size={32} />
            <Lufin count={15200} size={40} />
            <Lufin count={15200} size={52} />
          </div>
        </div>
        <Sidebar userRole='student' />
        <Sidebar userRole='teacher' />
      </div>
    </div>
  );
};

export default CommonComponents;
