import { useEffect, useState } from 'react';
import { Icon } from '../../../../components/Icon/Icon';
import Card from '../../../../components/Card/Card';
import { useDropdownMenu } from '../hooks/useDropdownMenu';
import { DropdownMenuItem } from './DropdownMenuItem';
import Button from '../../../../components/Button/Button';
import { fileService } from '../../../../libs/services/file/fileService';
import useAuthStore from '../../../../libs/store/authStore';

interface ClassroomCardProps {
  id: number;
  title: string;
  school: string;
  grade: string;
  students: number;
  year: number;
  imageKey?: string | null;
  showMenu?: boolean;
  isTeacher?: boolean;
  code?: string;
  onClick?: () => void;
  onEdit?: () => void;
  onDelete?: () => void;
}

const ClassroomCard = ({
  id,
  title,
  school,
  grade,
  students,
  year,
  imageKey,
  showMenu = true,
  code,
  onClick,
  onEdit,
  onDelete,
}: ClassroomCardProps) => {
  const { isOpen, menuRef, toggleMenu, closeMenu } = useDropdownMenu();
  const [imageUrl, setImageUrl] = useState<string>('');
  const [imageError, setImageError] = useState<boolean>(false);
  const [copySuccess, setCopySuccess] = useState(false);
  const { userRole } = useAuthStore();

  useEffect(() => {
    const loadImage = async () => {
      if (imageKey) {
        try {
          console.log('[ClassroomCard] 이미지 로딩 시작:', imageKey);
          const url = await fileService.getImageUrl(imageKey);
          console.log('[ClassroomCard] 이미지 URL 받음:', url);
          setImageUrl(url);
          setImageError(false);
        } catch (error) {
          console.error('[ClassroomCard] 이미지 로딩 실패:', error);
          setImageError(true);
        }
      }
    };
    loadImage();
  }, [imageKey]);

  const handleMenuItemClick = (handler?: () => void) => (e: React.MouseEvent) => {
    e.stopPropagation();
    handler?.();
    closeMenu();
  };

  const handleImageError = () => {
    console.error('[ClassroomCard] 이미지 렌더링 실패:', imageUrl);
    setImageError(true);
  };

  // 코드 복사 함수
  const handleCopyCode = (e: React.MouseEvent) => {
    e.stopPropagation(); // 클릭 이벤트가 카드까지 전파되지 않도록
    if (code) {
      navigator.clipboard
        .writeText(code)
        .then(() => {
          setCopySuccess(true);
          setTimeout(() => setCopySuccess(false), 2000);
        })
        .catch((err) => {
          console.error('클래스 코드 복사 실패:', err);
        });
    }
  };

  // 입장하기 버튼 클릭 핸들러
  const handleEntryClick = () => {
    // 카드 전체 클릭 이벤트와 분리하면서 id값 활용
    onClick?.();
  };

  const fallbackImageUrl = `https://picsum.photos/400/200?random=${year}`;

  return (
    <div onClick={onClick} data-classroom-id={id}>
      <Card className='cursor-pointer hover:shadow-lg transition-shadow'>
        <div className='flex flex-col gap-3'>
          <div className='relative'>
            <img
              src={imageError ? fallbackImageUrl : imageUrl || fallbackImageUrl}
              alt={title}
              className='w-full h-[160px] object-cover rounded-lg mb-3'
              onError={handleImageError}
            />
            {showMenu && userRole === 'TEACHER' && (
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
            {/* 교사 모드일 때만 초대 코드 복사 버튼 표시 */}
            {userRole === 'TEACHER' && code && (
              <button
                onClick={handleCopyCode}
                className='p-1 rounded-full hover:bg-grey/40 transition-colors flex items-center justify-center'
                title={copySuccess ? '복사되었습니다' : `초대 코드: ${code} (클릭하여 복사)`}
              >
                <Icon
                  name={copySuccess ? 'TickCircle' : 'Copy'}
                  size={24}
                  color={copySuccess ? 'success' : 'grey'}
                />
              </button>
            )}
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
            <Button color='info' variant='solid' size='md' onClick={handleEntryClick}>
              입장하기
            </Button>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default ClassroomCard;
