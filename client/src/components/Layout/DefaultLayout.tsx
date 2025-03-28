import { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const DefaultLayout = ({ children }: Props) => {
  return <div className='flex w-full h-screen bg-broken-white p-4'>{children}</div>;
};

export default DefaultLayout;
