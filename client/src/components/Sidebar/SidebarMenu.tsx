import Menu, { MenuType } from './Menu';
import { useLocation } from 'react-router-dom';
import { paths } from '../../routes/paths';

const menuList: MenuType[] = ['home', 'mission', 'wallet', 'chart', 'dollar', 'shop'];

interface SidebarMenuProps {
  userRole: 'student' | 'teacher';
}

const SidebarMenu = ({ userRole }: SidebarMenuProps) => {
  const location = useLocation();
  const pathMap: Record<MenuType, Partial<Record<'student' | 'teacher', string>>> = {
    home: {
      student: paths.STUDENT_DASHBOARD,
      teacher: paths.TEACHER_DASHBOARD,
    },
    mission: {
      student: paths.STUDENT_MISSION,
      teacher: paths.TEACHER_MISSION,
    },
    wallet: {
      // student: paths.STUDENT_STOCK,
      // teacher: paths.TEACHER_STOCK,
    },
    chart: {
      student: paths.STUDENT_STOCK,
      teacher: paths.TEACHER_STOCK,
    },
    dollar: {
      // student: paths.STUDENT_LOAN,
      // teacher: paths.TEACHER_LOAN,
    },
    shop: {
      student: paths.STUDENT_SHOP,
      teacher: paths.TEACHER_SHOP,
    },
  };

  const activeMenu =
    menuList.find((type) => location.pathname === pathMap[type][userRole]) ?? 'home';

  return (
    <div className='flex flex-col gap-2'>
      {menuList.map((type) => (
        <Menu key={type} type={type} isActive={activeMenu === type} userRole={userRole} />
      ))}
    </div>
  );
};

export default SidebarMenu;
