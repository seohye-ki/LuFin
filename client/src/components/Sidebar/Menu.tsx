import useClassroomStore from '../../libs/store/classroomStore';
import MenuItem, { MenuType } from './MenuItem';

const menuList: MenuType[] = ['dashboard', 'mission', 'shop', 'stock', 'loan'];

const Menu = () => {
  const { currentClassId } = useClassroomStore();
  return (
    currentClassId && (
      <div className='flex flex-col gap-2'>
        <p className='text-c1 font-semibold text-dark-grey'>메뉴</p>
        <div className='h-full flex flex-col gap-2'>
          {menuList.map((type) => (
            <MenuItem key={type} type={type} />
          ))}
        </div>
      </div>
    )
  );
};

export default Menu;
