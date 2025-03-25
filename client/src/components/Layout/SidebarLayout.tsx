import Sidebar from '../Sidebar/Sidebar';
import { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const SidebarLayout = ({ children }: Props) => {
  return (
    <div className='flex w-256 border'>
      <Sidebar userRole='student' />
      <main className='flex-1 p-4'>{children}</main>
    </div>
  );
};

export default SidebarLayout;
