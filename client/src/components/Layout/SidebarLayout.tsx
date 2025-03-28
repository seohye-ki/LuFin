import Sidebar from '../Sidebar/Sidebar';
import { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const SidebarLayout = ({ children }: Props) => {
  return (
    <div className='w-full max-w-[1920px] h-screen max-h-[1024px] mx-auto flex'>
      <Sidebar userRole='student' />
      <main className='w-full h-full p-4 bg-broken-white flex-1 overflow-hidden'>{children}</main>
    </div>
  );
};

export default SidebarLayout;
