import { Container } from './Container';
import lufinCoin from '../../../../assets/svgs/lufin-coin-102.svg';
import Button from '../../../../components/Button/Button';
import { Link } from 'react-router-dom';
import { paths } from '../../../../routes/paths';
import { Icon } from '../../../../components/Icon/Icon';
import tabletDashboardMission from '../../../../assets/images/mockups/tablet_dashboard_mission.png';

export function CallToAction() {
  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div 
      id="cta-section"
      className='relative isolate px-6 py-32 sm:py-40 lg:px-8 overflow-hidden'
    >
      {/* 백그라운드 장식 효과 */}
      <div className="absolute inset-0 -z-10">
        <div className="absolute right-1/2 bottom-0 -translate-y-1/2 transform-gpu blur-3xl sm:right-full sm:-translate-x-1/2 lg:bottom-0 lg:right-0 lg:translate-x-0 lg:translate-y-0" aria-hidden="true">
          <div className="aspect-[1108/632] w-[69.25rem] bg-gradient-to-r from-info/20 to-info/5 opacity-20" style={{ clipPath: "polygon(73.6% 48.6%, 91.7% 88.5%, 100% 53.9%, 97.4% 18.1%, 92.5% 15.4%, 75.7% 36.3%, 55.3% 52.8%, 46.5% 50.9%, 45% 37.4%, 50.3% 13.1%, 21.3% 36.2%, 0.1% 0.1%, 5.4% 49.1%, 21.4% 36.4%, 58.9% 100%, 73.6% 48.6%)" }} />
        </div>
      </div>

      <Container className='relative min-h-[720px] flex flex-col items-center justify-center'>
        <div className='grid grid-cols-1 lg:grid-cols-2 gap-12 items-center'>
          {/* 텍스트 영역 */}
          <div className='text-center lg:text-left order-2 lg:order-1'>
            <img src={lufinCoin} alt='루핀 코인' className='h-16 w-16 mb-6 mx-auto lg:mx-0' />
            <h2 className='font-pretendard text-h1 font-bold tracking-tight text-black'>
              루핀과 함께 
              <br />
              <span className='text-warning'>새로운 금융 교육</span>을 시작하세요
            </h2>
            <p className='mt-6 max-w-xl font-pretendard text-h3 font-medium text-grey'>
              실생활과 연계된 금융 교육으로 올바른 금융 습관을 형성하세요.
            </p>
            <div className='mt-10 flex flex-wrap items-center lg:justify-start justify-center gap-x-6 gap-y-4'>
              <Link to={paths.LOGIN}>
                <Button color='info'>무료로 시작하기</Button>
              </Link>
            </div>
          </div>
          
          {/* 이미지 영역 */}
          <div className='relative order-1 lg:order-2 flex justify-center'>
            <div className='relative w-full max-w-lg'>
              {/* 배경 효과 */}
              <div className='relative z-10 transform transition-all duration-500 hover:scale-105'>
                <img 
                  src={tabletDashboardMission} 
                  alt='루핀 미션 대시보드' 
                />
              </div>
            </div>
          </div>
        </div>
        
        <div className='absolute -bottom-20 left-1/2 transform -translate-x-1/2 cursor-pointer animate-gentle-bounce'>
          <button 
            onClick={scrollToTop}
            className='p-2 rounded-full bg-info/80 backdrop-blur-sm shadow-lg hover:bg-info transition-colors'
            aria-label='맨 위로 이동'
          >
            <Icon name='ArrowUp2' size={32} color='white' />
          </button>
        </div>
      </Container>
    </div>
  );
}
