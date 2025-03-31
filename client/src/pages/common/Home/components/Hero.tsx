import { Container } from './Container';
import { paths } from '../../../../routes/paths';
import logo from '../../../../assets/svgs/logo.svg';
import lufinCoin from '../../../../assets/svgs/lufin-coin-200.svg';
import Button from '../../../../components/Button/Button';
import { Link } from 'react-router-dom';
import tabletDashboard from '../../../../assets/images/mockups/tablet_dashboard_2.png';

export function Hero() {
  return (
    <Container className="pt-20 pb-16 lg:pt-32">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 lg:grid lg:grid-cols-12 lg:gap-x-8 lg:gap-y-20">
        <div className="lg:col-span-7 lg:text-left text-center">
          <div className="lg:text-left text-center mb-8">
            <img src={logo} alt="루핀 로고" className="h-12 w-auto lg:mx-0 mx-auto" />
          </div>
          <h1 className="font-pretendard text-5xl font-bold tracking-tight text-black">
            금융을{' '}
            <span className="relative whitespace-nowrap text-info">
              <svg
                aria-hidden="true"
                viewBox="0 0 418 42"
                className="absolute top-2/3 left-0 h-[0.58em] w-full fill-light-cyan-30"
                preserveAspectRatio="none"
              >
                <path d="M203.371.916c-26.013-2.078-76.686 1.963-124.73 9.946L67.3 12.749C35.421 18.062 18.2 21.766 6.004 25.934 1.244 27.561.828 27.778.874 28.61c.07 1.214.828 1.121 9.595-1.176 9.072-2.377 17.15-3.92 39.246-7.496C123.565 7.986 157.869 4.492 195.942 5.046c7.461.108 19.25 1.696 19.17 2.582-.107 1.183-7.874 4.31-25.75 10.366-21.992 7.45-35.43 12.534-36.701 13.884-2.173 2.308-.202 4.407 4.442 4.734 2.654.187 3.263.157 15.593-.78 35.401-2.686 57.944-3.488 88.365-3.143 46.327.526 75.721 2.23 130.788 7.584 19.787 1.924 20.814 1.98 24.557 1.332l.066-.011c1.201-.203 1.53-1.825.399-2.335-2.911-1.31-4.893-1.604-22.048-3.261-57.509-5.556-87.871-7.36-132.059-7.842-23.239-.254-33.617-.116-50.627.674-11.629.54-42.371 2.494-46.696 2.967-2.359.259 8.133-3.625 26.504-9.81 23.239-7.825 27.934-10.149 28.304-14.005.417-4.348-3.529-6-16.878-7.066Z" />
              </svg>
              <span className="relative">밝히다</span>
            </span>
          </h1>
          <p className="mt-6 font-pretendard text-h3 font-medium tracking-tight text-grey">
            <img src={lufinCoin} alt="루핀 코인" className="inline-block h-8 w-8 mr-2" />
            루핀으로 시작하는 새로운 금융 교육.
            <br />
            실생활과 연계된 금융 교육으로 올바른 금융 습관을 형성하세요.
          </p>
          <div className="mt-10 flex lg:justify-start justify-center gap-x-6">
            <Link to={paths.LOGIN}>
              <Button color="info">무료로 시작하기</Button>
            </Link>
          </div>
        </div>
        <div className="relative mt-10 lg:mt-0 lg:col-span-5">
          <div className="relative">
            <div className="absolute -inset-y-6 -inset-x-4 z-0 scale-95 bg-info/10 opacity-70 blur-2xl rounded-3xl" />
            <div className="relative">
              <img
                src={tabletDashboard}
                alt="루핀 대시보드"
                className="rounded-2xl shadow-xl ring-1 ring-slate-900/10"
              />
            </div>
          </div>
        </div>
      </div>
    </Container>
  );
} 