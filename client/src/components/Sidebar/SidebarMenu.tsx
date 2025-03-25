import { useState } from 'react';
import Menu, { MenuType } from './Menu';

const menuList: MenuType[] = ['home', 'mission', 'wallet', 'chart', 'dollar', 'shop'];

const SidebarMenu = () => {
  const [activeMenu, setActiveMenu] = useState<MenuType>('home');

  return (
    <div className='flex flex-col gap-2'>
      {menuList.map((type) => (
        <Menu
          key={type}
          type={type}
          isActive={activeMenu === type}
          onClick={() => setActiveMenu(type)}
        />
      ))}
    </div>
  );
};

export default SidebarMenu;
