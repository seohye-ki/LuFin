import { Container } from './Container';
import lufinCoin from '../../../../assets/svgs/lufin-coin-102.svg';
import Button from '../../../../components/Button/Button';
import { Link } from 'react-router-dom';
import { paths } from '../../../../routes/paths';

export function CallToAction() {
  return (
    <div className='relative isolate mt-32 px-6 py-32 sm:mt-56 sm:py-40 lg:px-8'>
      <svg
        className='absolute inset-0 -z-10 h-full w-full stroke-light-grey [mask-image:radial-gradient(100%_100%_at_top_right,white,transparent)]'
        aria-hidden='true'
      >
        <defs>
          <pattern
            id='1d4240dd-898f-445f-932d-e2872fd12de3'
            width={200}
            height={200}
            x='50%'
            y={0}
            patternUnits='userSpaceOnUse'
          >
            <path d='M.5 200V.5H200' fill='none' />
          </pattern>
        </defs>
        <rect
          width='100%'
          height='100%'
          strokeWidth={0}
          fill='url(#1d4240dd-898f-445f-932d-e2872fd12de3)'
        />
      </svg>
      <Container>
        <div className='mx-auto max-w-2xl text-center'>
          <img src={lufinCoin} alt='루핀 코인' className='mx-auto h-16 w-16 mb-6' />
          <h2 className='font-pretendard text-h1 font-bold tracking-tight text-black'>
            루핀과 함께 시작하세요
          </h2>
          <p className='mx-auto mt-6 max-w-xl font-pretendard text-h3 font-medium text-grey'>
            루핀으로 시작하는 새로운 금융 교육.
            <br />
            실생활과 연계된 금융 교육으로 올바른 금융 습관을 형성하세요.
          </p>
          <div className='mt-10 flex items-center justify-center gap-x-6'>
            <Link to={paths.LOGIN}>
              <Button color='info'>무료로 시작하기</Button>
            </Link>
            <Link to={paths.DESIGN_SYSTEM}>
              <Button variant='outline' color='info'>
                자세히 보기
              </Button>
            </Link>
          </div>
        </div>
      </Container>
    </div>
  );
}
