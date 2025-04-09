import Button from '../../../../components/Button/Button';
import lufinCoin from '../../../../assets/svgs/lufin-coin-200.svg';

interface CompletionProps {
  userType: 'teacher' | 'student' | null;
}

export default function Completion({ userType }: CompletionProps) {
  return (
    <div className='space-y-10 transition-all duration-300'>
      <div className='flex flex-col items-center text-center pb-10'>
        <div className='w-40 h-40 mb-8'>
          <img src={lufinCoin} alt='루핀 코인' className='w-full h-full' />
        </div>
        <h3 className='text-h2 font-bold text-black mb-3'>
          {userType === 'teacher' ? '선생님' : '학생'}으로 가입이 완료되었어요!
        </h3>
        <p className='text-h3 text-grey'>
          루핀과 함께 실전과 같은
          <br />
          금융 교육을 시작해보세요
        </p>
      </div>
      <div className='flex justify-center'>
        <Button
          type='button'
          color='primary'
          size='lg'
          full
          onClick={() => (window.location.href = '/login')}
        >
          로그인하러 가기
        </Button>
      </div>
    </div>
  );
}
