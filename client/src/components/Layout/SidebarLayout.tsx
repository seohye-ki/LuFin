import Sidebar from '../Sidebar/Sidebar';
import { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const SidebarLayout = ({ children }: Props) => {
  return (
    <div className='flex w-256'>
      <Sidebar userRole='student' />
      <main className='flex-1 p-4 bg-broken-white'>{children}</main>
    </div>
  );
};

export default SidebarLayout;
