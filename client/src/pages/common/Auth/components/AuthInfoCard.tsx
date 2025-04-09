import lufinCoin from '../../../../assets/svgs/lufin-coin-200.svg';

/**
 * 로그인/회원가입 페이지의 좌측 정보 영역 컴포넌트
 * 금융 교육에 대한 간략한 설명과 특징을 보여줍니다.
 */
export default function AuthInfoCard() {
  return (
    <div className='hidden lg:block relative w-1/2'>
      <div className='absolute inset-0 flex items-center justify-center p-8'>
        <div className='max-w-2xl text-center'>
          <div className='flex justify-center mb-8'>
            <img src={lufinCoin} alt='루핀 코인' className='w-32 h-32' />
          </div>
          <h3 className='text-h1 font-bold text-black'>금융 교육의 새로운 패러다임</h3>
          <p className='mt-4 text-h3 text-grey'>루핀과 함께 실전과 같은 금융 경험을 시작해보세요</p>
          <div className='mt-8 grid grid-cols-1 gap-4 sm:grid-cols-3'>
            <div className='rounded-2xl bg-white/10 backdrop-blur-sm p-4'>
              <p className='text-p1 font-semibold text-black'>신용등급 시스템</p>
            </div>
            <div className='rounded-2xl bg-white/10 backdrop-blur-sm p-4'>
              <p className='text-p1 font-semibold text-black'>AI 생성 투자 종목</p>
            </div>
            <div className='rounded-2xl bg-white/10 backdrop-blur-sm p-4'>
              <p className='text-p1 font-semibold text-black'>맞춤형 리포트</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
