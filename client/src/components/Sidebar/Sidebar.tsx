import logo from '../../assets/svgs/logo.svg';
import Profile from '../Profile/Profile';
import Lufin from '../Lufin/Lufin';
import SidebarMenu from './SidebarMenu';

interface SidebarProps {
  userRole: 'student' | 'teacher';
  className?: string;
}

const Sidebar = ({ userRole, className = '친구들의 금융 모임' }: SidebarProps) => {
  return (
    <div className='w-[200px] h-screen p-4 flex flex-col bg-white'>
      <div className='flex flex-col gap-4'>
        <div className='flex flex-col items-center gap-2'>
          <div className='flex flex-col gap-3'>
            <img src={logo} alt='루핀' className='h-[48px] py-2 justify-center' />
            <p className='text-c1 font-regular text-dark-grey'>클래스</p>
            <p className='text-c1 font-medium'>{className}</p>
            <hr className='w-full border-t border-new-grey' />
            <p className='text-c1 font-regular text-dark-grey'>메뉴</p>
            <SidebarMenu />
          </div>
        </div>
      </div>

      <div className='mt-auto flex flex-col gap-4'>
        {userRole === 'student' && (
          <div className='flex w-full h-21 flex-col bg-yellow rounded-lg p-4 gap-1'>
            <p className='text-c1 text-dark-grey'>총 자산</p>
            <Lufin count={15200} size='l' />
          </div>
        )}

        <Profile
          name='이재현'
          variant='row'
          certificationNumber='5학년 1반 12번'
          profileImage='https://picsum.photos/200/300?random=1'
          className='border border-purple-30 rounded-lg'
        />
      </div>
    </div>
  );
};

export default Sidebar;
