import { twMerge } from 'tailwind-merge';
import lufinCoin16 from '../../assets/svgs/lufin-coin-16.svg';
import lufinCoin24 from '../../assets/svgs/lufin-coin-24.svg';
import lufinCoin32 from '../../assets/svgs/lufin-coin-32.svg';

interface LufinProps {
  size?: 's' | 'm' | 'l';
  count?: number;
  className?: string;
}

const lufinCoinMap = {
  s: lufinCoin16,
  m: lufinCoin24,
  l: lufinCoin32,
};

const sizeMap = {
  s: 'w-4 h-4',
  m: 'w-6 h-6',
  l: 'w-8 h-8',
};

const fontSizeMap = {
  s: 'text-p2 font-semibold',
  m: 'text-h3 font-semibold',
  l: 'text-h2 font-semibold',
};

const Lufin = ({ size = 'l', count, className }: LufinProps) => {
  const lufinSrc = lufinCoinMap[size];
  const lufinSize = sizeMap[size];
  const fontSize = fontSizeMap[size];

  return (
    <div className={twMerge('flex items-center gap-1', className)}>
      <img src={lufinSrc} alt='루핀 코인' className={lufinSize} />
      {count !== undefined && <span className={fontSize}>{count}</span>}
    </div>
  );
};

export default Lufin;
