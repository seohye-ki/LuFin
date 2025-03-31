import { ReactNode } from 'react';

type CardProps = {
  titleLeft?: string;
  titleRight?: string | ReactNode;
  titleSize?: 's' | 'm' | 'l';
  children?: ReactNode;
  className?: string;
  isModal?: boolean;
};

const Card = ({
  titleLeft,
  titleRight,
  titleSize = 'l',
  children,
  className,
  isModal = false,
}: CardProps) => {
  const titleStyle = {
    s: 'text-c1 font-semibold text-grey',
    m: 'text-p2 font-semibold text-black',
    l: 'text-h3 font-semibold text-black',
  };

  return (
    <div className={`flex flex-col rounded-xl p-4 gap-4 bg-white ${className}`}>
      {(titleLeft || titleRight) && (
        <div className='flex h-fit justify-between items-center'>
          <span className={titleStyle[titleSize]}>{titleLeft}</span>
          {titleRight && <span className={titleStyle[titleSize]}>{titleRight}</span>}
        </div>
      )}
      {isModal && <hr className='border-t border-new-grey' />}
      <div className='h-full flex flex-col gap-4'>{children}</div>
    </div>
  );
};

export default Card;
