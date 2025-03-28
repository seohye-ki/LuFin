import { ReactNode } from 'react';

type CardProps = {
  titleLeft?: string;
  titleRight?: string | ReactNode;
  titleSize?: 's' | 'm' | 'l';
  content?: ReactNode;
  className?: string;
  isModal?: boolean;
};

const Card = ({
  titleLeft,
  titleRight,
  titleSize = 'l',
  content,
  className,
  isModal = false,
}: CardProps) => {
  const titleStyle = {
    s: 'text-c1 font-semibold text-grey',
    m: 'text-p2 font-semibold text-black',
    l: 'text-h3 font-semibold text-black',
  };

  return (
    <div className={`rounded-xl w-full h-full bg-white ${className}`}>
      {(titleLeft || titleRight) && (
        <div className='flex h-16 gap-4 p-4 justify-between items-center'>
          <span className={titleStyle[titleSize]}>{titleLeft}</span>
          {titleRight && <span className={titleStyle[titleSize]}>{titleRight}</span>}
        </div>
      )}
      {isModal && <hr className='border-t border-new-grey' />}
      <div className='flex flex-col gap-3 p-4'>{content}</div>
    </div>
  );
};

export default Card;
