import { Link } from 'react-router-dom';
import { paths } from '../../../../routes/paths';
import logo from '../../../../assets/svgs/logo.svg';
import Button from '../../../../components/Button/Button';
import useAuthStore from '../../../../libs/store/authStore';
import { hideGlobalAlert, showGlobalAlert } from '../../../../libs/store/alertStore';

export function Header() {
  const { isAuthenticated, logout } = useAuthStore();

  const handleLogout = () => {
    showGlobalAlert(
      '로그아웃 하시겠습니까?',
      null,
      '',
      'warning',
      {
        label: '확인',
        onClick: () => {
          showGlobalAlert(
            '로그아웃 했어요.',
            null,
            '확인을 누르면 로그인 페이지로 이동합니다.',
            'info',
            {
              label: '확인',
              onClick: () => {
                logout(); // 실제 상태 초기화는 여기서
                hideGlobalAlert();
                window.location.href = '/login'; // 진짜 리다이렉션도 여기서
              },
            },
          );
        },
      },
      { label: '취소', onClick: hideGlobalAlert },
    );
  };
  return (
    <header className='absolute inset-x-0 top-0 z-50'>
      <nav className='flex items-center justify-between p-6 lg:px-8' aria-label='Global'>
        <div className='flex flex-1'>
          <Link to={paths.HOME} className='-m-1.5 p-1.5'>
            <span className='sr-only'>루핀</span>
            <img className='h-8 w-auto' src={logo} alt='루핀' />
          </Link>
        </div>
        <div className='flex flex-1 justify-end'>
          {isAuthenticated ? (
            <Button color='info' onClick={handleLogout}>
              로그아웃
            </Button>
          ) : (
            <Link to={paths.LOGIN}>
              <Button color='info'>로그인</Button>
            </Link>
          )}
        </div>
      </nav>
    </header>
  );
}
