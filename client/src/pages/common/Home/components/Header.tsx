import { useState } from 'react';
import { Link } from 'react-router-dom';
import { paths } from '../../../../routes/paths';
import logo from '../../../../assets/svgs/logo.svg';
import Button from '../../../../components/Button/Button';

export function Header() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  return (
    <header className="absolute inset-x-0 top-0 z-50">
      <nav className="flex items-center justify-between p-6 lg:px-8" aria-label="Global">
        <div className="flex lg:flex-1">
          <Link to={paths.HOME} className="-m-1.5 p-1.5">
            <span className="sr-only">루핀</span>
            <img className="h-8 w-auto" src={logo} alt="루핀" />
          </Link>
        </div>
        <div className="flex lg:hidden">
          <button
            type="button"
            className="-m-2.5 inline-flex items-center justify-center rounded-md p-2.5 text-grey"
            onClick={() => setMobileMenuOpen(true)}
          >
            <span className="sr-only">메뉴 열기</span>
            <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />
            </svg>
          </button>
        </div>
        <div className="hidden lg:flex lg:flex-1 lg:justify-end">
          <Link to={paths.LOGIN}>
            <Button color="info">로그인</Button>
          </Link>
        </div>
      </nav>
      <div className={`lg:hidden ${mobileMenuOpen ? 'fixed inset-0 z-50' : 'hidden'}`}>
        <div className="fixed inset-0 bg-black/80" onClick={() => setMobileMenuOpen(false)} />
        <div className="fixed inset-y-0 right-0 z-50 w-full overflow-y-auto bg-white px-6 py-6 sm:max-w-sm sm:ring-1 sm:ring-grey-30">
          <div className="flex items-center justify-between">
            <Link to={paths.HOME} className="-m-1.5 p-1.5">
              <span className="sr-only">루핀</span>
              <img className="h-8 w-auto" src={logo} alt="루핀" />
            </Link>
            <button
              type="button"
              className="-m-2.5 rounded-md p-2.5 text-grey"
              onClick={() => setMobileMenuOpen(false)}
            >
              <span className="sr-only">메뉴 닫기</span>
              <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <div className="mt-6 flow-root">
            <div className="-my-6 divide-y divide-grey-30">
              <div className="py-6">
                <Link to={paths.LOGIN} onClick={() => setMobileMenuOpen(false)}>
                  <Button color="info">로그인</Button>
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
} 