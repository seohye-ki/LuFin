import { twMerge } from 'tailwind-merge';
import lufinCoin16 from '../../assets/svgs/lufin-coin-16.svg';
import lufinCoin24 from '../../assets/svgs/lufin-coin-24.svg';
import lufinCoin32 from '../../assets/svgs/lufin-coin-32.svg';
import lufinCoin40 from '../../assets/svgs/lufin-coin-40.svg';
import lufinCoin52 from '../../assets/svgs/lufin-coin-52.svg';

interface LufinProps {
  size?: 16 | 24 | 32 | 40 | 52;
  count?: number;
  className?: string;
}

const lufinCoinMap = {
  16: lufinCoin16,
  24: lufinCoin24,
  32: lufinCoin32,
  40: lufinCoin40,
  52: lufinCoin52,
};

const sizeMap = {
  16: 'w-4 h-4',
  24: 'w-6 h-6',
  32: 'w-8 h-8',
  40: 'w-10 h-10',
  52: 'w-12 h-12',
};

const fontSizeMap = {
  16: 'text-p3 semibold',
  24: 'text-p2 semibold',
  32: 'text-p1 semibold',
  40: 'text-h2 semibold',
  52: 'text-h1 semibold',
};

const Lufin = ({ size = 40, count, className }: LufinProps) => {
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
