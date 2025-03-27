import { ReactNode } from 'react';

interface Props {
  children: ReactNode;
}

const DefaultLayout = ({ children }: Props) => {
  return <div className='w-256 bg-broken-white p-4'>{children}</div>;
};

export default DefaultLayout;
