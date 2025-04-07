import { Icon } from '../Icon/Icon';
import { twMerge } from 'tailwind-merge';
import type { IconsaxIconName } from '../Icon/Icon';
import { useNavigate } from 'react-router-dom';
import { paths } from '../../routes/paths';
export type MenuType = 'home' | 'mission' | 'wallet' | 'chart' | 'dollar' | 'shop';

interface MenuProps {
  isActive?: boolean;
  type: MenuType;
  userRole: 'student' | 'teacher';
}

const menuConfig: Record<MenuType, { icon: IconsaxIconName; text: string }> = {
  home: {
    icon: 'Home2',
    text: '홈',
  },
  mission: {
    icon: 'TaskSquare',
    text: '미션',
  },
  wallet: {
    icon: 'Wallet',
    text: '적금',
  },
  chart: {
    icon: 'ChartSquare',
    text: '투자',
  },
  dollar: {
    icon: 'DollarSquare',
    text: '대출',
  },
  shop: {
    icon: 'Shop',
    text: '상점',
  },
};

const Menu = ({ isActive = false, type, userRole }: MenuProps) => {
  const { icon, text } = menuConfig[type];
  const navigate = useNavigate();

  const pathMap: Record<MenuType, Partial<Record<'student' | 'teacher', string>>> = {
    home: {
      student: paths.STUDENT_DASHBOARD,
      // teacher: paths.TEACHER_DASHBOARD,
    },
    mission: {
      student: paths.STUDENT_MISSION,
      teacher: paths.TEACHER_MISSION,
    },
    wallet: {
      student: paths.STUDENT_STOCK,
      // teacher: paths.TEACHER_STOCK,
    },
    chart: {
      student: paths.STUDENT_STOCK,
      teacher: paths.TEACHER_STOCK,
    },
    dollar: {
      student: paths.STUDENT_LOAN,
      // teacher: paths.TEACHER_LOAN,
    },
    shop: {
      student: paths.STUDENT_SHOP,
      teacher: paths.TEACHER_SHOP,
    },
  };

  const handleClick = () => {
    if (pathMap[type][userRole]) {
      navigate(pathMap[type][userRole]);
    }
  };

  return (
    <button
      className={twMerge(
        'flex items-center gap-3 w-[168px] h-[40px] p-2 rounded-lg text-p2 font-medium hover:bg-light-cyan-30',
        isActive && 'bg-light-cyan',
      )}
      onClick={handleClick}
    >
      <Icon name={icon} size={24} color={isActive ? 'black' : 'grey'} />
      <span className={twMerge(isActive ? 'text-black' : 'text-grey')}>{text}</span>
    </button>
  );
};

export default Menu;
