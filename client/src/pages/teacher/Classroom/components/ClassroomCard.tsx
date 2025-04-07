import { useEffect, useState } from 'react';
import { Icon } from '../../../../components/Icon/Icon';
import Card from '../../../../components/Card/Card';
import { useDropdownMenu } from '../hooks/useDropdownMenu';
import { DropdownMenuItem } from './DropdownMenuItem';
import Button from '../../../../components/Button/Button';
import { fileService } from '../../../../libs/services/file/fileService';

interface ClassroomCardProps {
  id: number;
  title: string;
  school: string;
  grade: string;
  students: number;
  year: number;
  imageKey?: string | null;
  showMenu?: boolean;
  onClick?: () => void;
  onEdit?: () => void;
  onDelete?: () => void;
}

const ClassroomCard = ({
  title,
  school,
  grade,
  students,
  year,
  imageKey,
  showMenu = true,
  onClick,
  onEdit,
  onDelete,
}: ClassroomCardProps) => {
  const { isOpen, menuRef, toggleMenu, closeMenu } = useDropdownMenu();
  const [imageUrl, setImageUrl] = useState<string>('');

  useEffect(() => {
    const loadImage = async () => {
      if (imageKey) {
        const url = await fileService.getImageUrl(imageKey);
        setImageUrl(url);
      }
    };
    loadImage();
  }, [imageKey]);

  const handleMenuItemClick = (handler?: () => void) => (e: React.MouseEvent) => {
    e.stopPropagation();
    handler?.();
    closeMenu();
  };

  return (
    <div onClick={onClick}>
      <Card className='cursor-pointer hover:shadow-lg transition-shadow'>
        <div className='flex flex-col gap-3'>
          <div className='relative'>
            <img
              src={imageUrl || `https://picsum.photos/400/200?random=${year}`}
              alt={title}
              className='w-full h-[160px] object-cover rounded-lg mb-3'
            />
            {showMenu && (
              <div className='absolute top-2 right-2 flex items-center gap-2'>
                <div className='relative' ref={menuRef}>
                  <button
                    onClick={toggleMenu}
                    className='p-1 rounded-full bg-white/80 hover:bg-white transition-colors'
                  >
                    <div className='rotate-90'>
                      <Icon name='More' size={20} color='grey' />
                    </div>
                  </button>
                  {isOpen && (
                    <div className='absolute right-0 mt-1 w-24 bg-white rounded-lg shadow-lg py-1 z-10'>
                      <DropdownMenuItem label='수정' onClick={handleMenuItemClick(onEdit)} />
                      <DropdownMenuItem label='삭제' onClick={handleMenuItemClick(onDelete)} />
                    </div>
                  )}
                </div>
              </div>
            )}
          </div>
          <div className='flex justify-between items-center'>
            <h3 className='text-p1 font-semibold'>{title}</h3>
          </div>
          <div className='flex flex-col gap-2 text-p2 text-dark-grey'>
            <p>{school}</p>
            <p>{grade}</p>
            <div className='flex items-center gap-2'>
              <Icon name='Profile2User' size={16} color='grey' />
              <span>{students}명</span>
            </div>
          </div>
          <div className='mt-auto pt-4 flex justify-between items-center text-p1'>
            <span className='text-grey'>{year}</span>
            <Button
              color='info'
              variant='solid'
              size='md'
              onClick={() => {
                // 입장하기 버튼만의 별도 동작이 필요한 경우 여기에 추가
              }}
            >
              입장하기
            </Button>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default ClassroomCard;
