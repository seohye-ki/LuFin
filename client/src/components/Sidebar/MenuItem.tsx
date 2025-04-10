import { useNavigate, useLocation } from 'react-router-dom';
import { twMerge } from 'tailwind-merge';
import { Icon } from '../Icon/Icon';
import type { IconsaxIconName } from '../Icon/Icon';
import { paths } from '../../routes/paths';
import useClassroomStore from '../../libs/store/classroomStore';
import useAlertStore from '../../libs/store/alertStore';

export type MenuType = 'dashboard' | 'mission' | 'shop' | 'stock' | 'loan';

const menuMap: Record<MenuType, { icon: IconsaxIconName; text: string; path: string }> = {
  dashboard: { icon: 'Home2', text: '대시보드', path: paths.DASHBOARD },
  mission: { icon: 'TaskSquare', text: '미션', path: paths.MISSION },
  stock: { icon: 'ChartSquare', text: '투자', path: paths.STOCK },
  loan: { icon: 'DollarSquare', text: '대출', path: paths.LOAN },
  shop: { icon: 'Shop', text: '상점', path: paths.SHOP },
};

interface MenuItemProps {
  type: MenuType;
}

const MenuItem = ({ type }: MenuItemProps) => {
  const { currentClassId } = useClassroomStore();
  const { showAlert, hideAlert } = useAlertStore();
  const { icon, text, path } = menuMap[type];
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = location.pathname === path;

  const handleClick = () => {
    if (currentClassId === null) {
      showAlert('선택한 클래스가 없습니다.', null, '클래스를 먼저 선택해주세요.', 'danger', {
        label: '확인',
        color: 'danger',
        onClick: () => hideAlert(),
      });
      navigate(paths.CLASSROOM);
    } else {
      navigate(path);
    }
  };

  return (
    <button
      onClick={handleClick}
      className={twMerge(
        'w-full flex items-center gap-3  h-[40px] p-2 rounded-lg text-p2 font-medium hover:bg-light-cyan-30',
        isActive && 'bg-light-cyan',
      )}
    >
      <Icon name={icon} size={24} color={isActive ? 'black' : 'grey'} />
      <span className={isActive ? 'text-black' : 'text-grey'}>{text}</span>
    </button>
  );
};

export default MenuItem;
