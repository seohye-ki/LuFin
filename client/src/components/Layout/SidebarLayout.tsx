import Sidebar from '../Sidebar/Sidebar';
import { ReactNode } from 'react';

interface Props {
  children: ReactNode;
  mainRef?: React.RefObject<HTMLDivElement | null>;
  userRole?: 'student' | 'teacher';
  hideMenu?: boolean;
}

const SidebarLayout = ({ children, userRole = 'student', hideMenu }: Props) => {
  return (
    <div className='w-full max-w-[1920px] h-screen max-h-[1024px] mx-auto flex'>
      {userRole === 'student' && <Sidebar userRole='student' hideMenu={hideMenu} />}
      {userRole === 'teacher' && <Sidebar userRole='teacher' hideMenu={hideMenu} />}
      <main className='w-full h-full p-4 bg-broken-white flex-1 overflow-hidden'>{children}</main>
    </div>
  );
};

export default SidebarLayout;
