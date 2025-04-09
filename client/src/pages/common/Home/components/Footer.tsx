import { Container } from './Container';
import logo from '../../../../assets/svgs/logo.svg';

export function Footer() {
  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <footer className='bg-broken-white'>
      <Container>
        <div className='pt-16'>
          <div onClick={scrollToTop}>
            <img className='mx-auto h-10 w-auto cursor-pointer hover:opacity-80 transition-opacity' src={logo} alt='루핀' />
          </div>
        </div>
        <div className='flex flex-col items-center border-t border-grey-30 py-10 sm:justify-between'>
          <p className='mt-6 font-pretendard text-p2 text-grey sm:mt-0'>
            Copyright &copy; {new Date().getFullYear()} 루핀. All rights reserved.
          </p>
        </div>
      </Container>
    </footer>
  );
}
