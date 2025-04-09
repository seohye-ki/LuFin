import logo from '../../assets/svgs/logo.svg';
import Profile from '../Profile/Profile';
import Lufin from '../Lufin/Lufin';
import SidebarMenu from './SidebarMenu';
import { paths } from '../../routes/paths';
import { Link, useLocation } from 'react-router-dom';
import useClassroomStore from '../../libs/store/classroomStore';
import { useEffect, useState } from 'react';
import { Icon } from '../Icon/Icon';
import { DashboardService } from '../../libs/services/dashboard/dashboardService';
import { tokenUtils } from '../../libs/services/axios';
import { hideGlobalAlert, showGlobalAlert } from '../../libs/store/alertStore';

interface SidebarProps {
  userRole: 'student' | 'teacher';
  hideMenu?: boolean;
}

const Sidebar = ({ userRole, hideMenu = false }: SidebarProps) => {
  const { fetchClassrooms, getCurrentClassroom, getClassCode, classCode, currentClassName } =
    useClassroomStore();
  const [copySuccess, setCopySuccess] = useState(false);
  const [totalAssets, setTotalAssets] = useState<number>(0);
  const location = useLocation();

  // 페이지 로드 시 클래스룸 정보 가져오기
  useEffect(() => {
    fetchClassrooms();
    // 교사인 경우 클래스 코드 가져오기
    if (userRole === 'teacher') {
      getClassCode();
    }
  }, [fetchClassrooms, getClassCode, userRole]);

  // 학생인 경우 대시보드 데이터 가져오기
  useEffect(() => {
    const fetchStudentDashboard = async () => {
      if (userRole === 'student' && !hideMenu) {
        try {
          const response = await DashboardService.getStudentDashboard();
          if (response.isSuccess) {
            setTotalAssets(response.data.totalAssets);
          }
        } catch (error) {
          console.error('Failed to fetch student dashboard:', error);
        }
      }
    };

    fetchStudentDashboard();
  }, [userRole, hideMenu]);

  const logout = () => {
    showGlobalAlert(
      '로그아웃 하시겠습니까?',
      null,
      '확인을 누르면 로그인 페이지로 이동합니다.',
      'warning',
      {
        label: '확인',
        onClick: () => {
          tokenUtils.removeToken('accessToken');
          tokenUtils.removeToken('refreshToken');
          tokenUtils.removeToken('userRole');
          window.location.href = '/login';
        },
      },
      {
        label: '취소',
        onClick: () => {
          hideGlobalAlert();
        },
      },
    );
  };

  // 현재 활성화된 클래스 확인
  const currentClassroom = getCurrentClassroom();

  // 클래스룸 페이지인지 확인
  const isClassroomPage =
    location.pathname === paths.STUDENT_CLASSROOM || location.pathname === paths.TEACHER_CLASSROOM;

  // 클래스 정보 표시 내용 결정
  const classInfoText = isClassroomPage
    ? '클래스를 선택하세요'
    : userRole === 'teacher'
      ? currentClassroom?.name || '클래스 없음'
      : currentClassName || '클래스 없음';

  // 클래스 코드 복사 함수
  const handleCopyCode = () => {
    if (classCode) {
      navigator.clipboard
        .writeText(classCode)
        .then(() => {
          setCopySuccess(true);
          setTimeout(() => setCopySuccess(false), 2000);
        })
        .catch((err) => {
          console.error('클래스 코드 복사 실패:', err);
        });
    }
  };

  return (
    <div className='w-[200px] h-full p-4 flex flex-col bg-white'>
      <div className='flex flex-col gap-4'>
        <div className='flex flex-col items-center gap-2'>
          <div className='flex flex-col gap-3'>
            <img src={logo} alt='루핀' className='h-[48px] py-2 justify-center' />
            {userRole === 'student' && (
              <Link to={paths.STUDENT_CLASSROOM}>
                <p className='text-c1 font-regular text-dark-grey'>클래스</p>
                <p className='text-h3 font-medium'>{classInfoText}</p>
              </Link>
            )}
            {userRole === 'teacher' && (
              <>
                <Link to={paths.TEACHER_CLASSROOM}>
                  <p className='text-c1 font-regular text-dark-grey'>클래스</p>
                  <p className='text-h3 font-medium'>{classInfoText}</p>
                </Link>
                {currentClassroom && !isClassroomPage && (
                  <div className='mt-2 flex flex-col gap-2'>
                    <p className='text-c1 font-regular text-dark-grey'>초대 코드</p>
                    <div className='flex items-center gap-1'>
                      <div className='bg-broken-white px-2 py-1 rounded-md font-mono text-sm font-bold flex-1 truncate'>
                        {classCode || '로딩 중...'}
                      </div>
                      <button
                        onClick={handleCopyCode}
                        className='p-1 hover:bg-broken-white rounded-md transition-colors'
                      >
                        <Icon
                          name={copySuccess ? 'TickCircle' : 'Copy'}
                          size={16}
                          color={copySuccess ? 'success' : 'info'}
                        />
                      </button>
                    </div>
                  </div>
                )}
              </>
            )}
            {!hideMenu && (
              <>
                <hr className='w-full border-t border-new-grey' />
                <p className='text-c1 font-regular text-dark-grey'>메뉴</p>
                <SidebarMenu userRole={userRole} />
              </>
            )}
          </div>
        </div>
      </div>

      <div className='mt-auto flex flex-col gap-4'>
        {userRole === 'student' && !hideMenu && (
          <div className='flex w-full h-21 flex-col bg-yellow rounded-lg p-4 gap-1'>
            <p className='text-c1 text-dark-grey'>총 자산</p>
            <Lufin count={totalAssets} size='l' />
          </div>
        )}

        <div onClick={logout}>
          <Profile
            name='이재현'
            variant='row'
            certificationNumber='5학년 1반 12번'
            profileImage='https://picsum.photos/200/300?random=1'
            className='border border-purple-30 rounded-lg'
          />
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
