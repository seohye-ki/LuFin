import { Link } from 'react-router-dom';
import { paths } from '../../../../routes/paths';
import logo from '../../../../assets/svgs/logo.svg';
import Button from '../../../../components/Button/Button';
import useAuthStore from '../../../../libs/store/authStore';

export function Header() {
  const { isAuthenticated, logout } = useAuthStore();

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
            <Button color='info' onClick={logout}>
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
