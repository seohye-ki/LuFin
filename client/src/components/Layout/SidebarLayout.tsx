import Sidebar from '../Sidebar/Sidebar';
import { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const SidebarLayout = ({ children }: Props) => {
  return (
    <div className='flex w-full h-screen'>
      <Sidebar userRole='student' />
      <main className='flex-1 p-4 bg-broken-white h-screen overflow-auto'>{children}</main>
    </div>
  );
};

export default SidebarLayout;
