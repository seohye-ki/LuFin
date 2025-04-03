import Sidebar from '../Sidebar/Sidebar';
import { ReactNode } from 'react';

interface Props {
  children: ReactNode;
  mainRef?: React.RefObject<HTMLDivElement | null>;
  userRole?: 'student' | 'teacher';
}

const SidebarLayout = ({ children, userRole = 'student' }: Props) => {
  return (
    <div className='w-full max-w-[1920px] h-screen max-h-[1024px] mx-auto flex'>
      {userRole === 'student' && <Sidebar userRole='student' />}
      {userRole === 'teacher' && <Sidebar userRole='teacher' />}
      <main className='w-full h-full p-4 bg-broken-white flex-1 overflow-hidden'>{children}</main>
    </div>
  );
};

export default SidebarLayout;
