import home from '../../assets/svgs/home-default.svg';
import mission from '../../assets/svgs/mission-default.svg';
import wallet from '../../assets/svgs/wallet-default.svg';
import chart from '../../assets/svgs/chart-default.svg';
import dollar from '../../assets/svgs/dollar-default.svg';
import shop from '../../assets/svgs/shop-default.svg';
import homeGrey from '../../assets/svgs/home-grey.svg';
import missionGrey from '../../assets/svgs/mission-grey.svg';
import walletGrey from '../../assets/svgs/wallet-grey.svg';
import chartGrey from '../../assets/svgs/chart-grey.svg';
import dollarGrey from '../../assets/svgs/dollar-grey.svg';
import shopGrey from '../../assets/svgs/shop-grey.svg';
import { twMerge } from 'tailwind-merge';

export type MenuType = 'home' | 'mission' | 'wallet' | 'chart' | 'dollar' | 'shop';

interface MenuProps {
  isActive?: boolean;
  type: MenuType;
  onClick?: () => void;
}

const menuConfig = {
  home: {
    active: home,
    inactive: homeGrey,
    text: '홈',
  },
  mission: {
    active: mission,
    inactive: missionGrey,
    text: '미션',
  },
  wallet: {
    active: wallet,
    inactive: walletGrey,
    text: '적금',
  },
  chart: {
    active: chart,
    inactive: chartGrey,
    text: '투자',
  },
  dollar: {
    active: dollar,
    inactive: dollarGrey,
    text: '대출',
  },
  shop: {
    active: shop,
    inactive: shopGrey,
    text: '상점',
  },
};

const Menu = ({ isActive = false, type, onClick }: MenuProps) => {
  const { active: activeIcon, inactive: inactiveIcon, text } = menuConfig[type];
  const iconSrc = isActive ? activeIcon : inactiveIcon;
  const textColor = isActive ? 'text-black' : 'text-grey';

  return (
    <button
      className={twMerge(
        'flex text-p2 font-medium items-center gap-3 w-[168px] h-[40px] p-2 rounded-lg',
        isActive && 'bg-light-cyan',
      )}
      onClick={onClick}
    >
      <img src={iconSrc} alt={text} />
      <p className={textColor}>{text}</p>
    </button>
  );
};

export default Menu;
