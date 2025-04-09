import { Link } from 'react-router-dom';
import { paths } from '../../routes/paths';
import useClassroomStore from '../../libs/store/classroomStore';
import { Icon } from '../Icon/Icon';
import useAuthStore from '../../libs/store/authStore';
import { useState } from 'react';

const ClassInfo = () => {
  const { currentClassId, currentClassName, currentClassCode, resetCurrentClass } =
    useClassroomStore();
  const { userRole } = useAuthStore();
  const [copySuccess, setCopySuccess] = useState(false);

  const handleCopyCode = () => {
    if (!currentClassCode) return;
    navigator.clipboard
      .writeText(currentClassCode)
      .then(() => {
        setCopySuccess(true);
        setTimeout(() => setCopySuccess(false), 2000);
      })
      .catch((err) => console.error('클래스 코드 복사 실패:', err));
  };

  return (
    <>
      <Link to={paths.CLASSROOM} className='w-full flex flex-col gap-1' onClick={resetCurrentClass}>
        <p className='text-c1 font-semibold text-dark-grey'>클래스</p>
        <div className='bg-broken-white px-2 py-2 rounded-md flex flex-row justify-between items-center'>
          <p className='text-p2 text-black font-medium'>
            {currentClassName || '클래스를 선택하세요.'}
          </p>
          <Icon name='ArrangeHorizontal' size={14} color='dark-grey' />
        </div>
      </Link>

      {userRole === 'TEACHER' && currentClassId && (
        <div className='w-full flex flex-col gap-1 mt-2'>
          <p className='text-c1 font-semibold text-dark-grey'>초대 코드</p>
          <div
            className='bg-broken-white px-2 py-2 rounded-md flex flex-row justify-between items-center cursor-pointer'
            onClick={handleCopyCode}
          >
            <p className='text-p2 text-black font-medium'>{currentClassCode || '로딩 중...'}</p>
            <Icon
              name={copySuccess ? 'TickCircle' : 'Copy'}
              size={14}
              color={copySuccess ? 'success' : 'dark-grey'}
            />
          </div>
        </div>
      )}
    </>
  );
};

export default ClassInfo;
