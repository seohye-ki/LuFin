import logo from '../../assets/svgs/logo.svg';
import SidebarMenu from './Menu';
import MemberInfo from './MemberInfo';
import ClassInfo from './ClassInfo';

const Sidebar = () => {
  return (
    <div className='w-[200px] h-full p-4 bg-white flex flex-col '>
      <div className='h-full flex flex-col items-start gap-4 flex-1'>
        <img src={logo} alt='루핀' className='w-full h-10 py-2 justify-center' />
        <ClassInfo />
        <div className='h-full flex flex-col justify-between'>
          <SidebarMenu />
          <MemberInfo />
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
