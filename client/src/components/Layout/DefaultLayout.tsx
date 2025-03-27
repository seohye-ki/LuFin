import { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const DefaultLayout = ({ children }: Props) => {
  return <div className='w-full bg-broken-white p-4'>{children}</div>;
};

export default DefaultLayout;
