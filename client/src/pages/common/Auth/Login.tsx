import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '../../../components/Button/Button';
import TextField from '../../../components/Form/TextField';
import logo from '../../../assets/svgs/logo.svg';
import lufinCoin from '../../../assets/svgs/lufin-coin-200.svg';
import useAuthStore from '../../../libs/store/authStore';
import { paths } from '../../../routes/paths';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const navigate = useNavigate();

  // Zustand 스토어에서 필요한 상태와 액션 가져오기
  const { login, isLoading, error } = useAuthStore();

  // 이메일 유효성 검사
  const validateEmail = (email: string): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isValid = emailRegex.test(email);
    setEmailError(isValid ? '' : '유효한 이메일 주소를 입력해주세요');
    return isValid;
  };

  // 비밀번호 유효성 검사
  const validatePassword = (password: string): boolean => {
    const isValid = password.length >= 1;
    setPasswordError(isValid ? '' : '비밀번호를 입력해주세요');
    return isValid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // 유효성 검사
    const isEmailValid = validateEmail(email);
    const isPasswordValid = validatePassword(password);

    if (!isEmailValid || !isPasswordValid) {
      return;
    }

    console.log('로그인 시도:', { email, password });

    const result = await login({ email, password });

    console.log('로그인 결과:', result);

    if (result.success) {
      console.log('로그인 성공, 역할:', result.role);
      // 역할에 따라 다른 페이지로 이동
      if (result.role === 'TEACHER') {
        navigate(paths.TEACHER_CLASSROOM);
      } else {
        // 기본적으로 학생(STUDENT) 대시보드로 이동
        navigate(paths.STUDENT_DASHBOARD);
      }
    } else {
      console.log('로그인 실패:', result.message);
    }
  };

  return (
    <div className='relative flex min-h-screen bg-gradient-to-br from-light-cyan-30 via-info-30 to-yellow-30'>
      <div className="absolute inset-0 bg-[url('/grid.svg')] bg-center [mask-image:linear-gradient(180deg,white,rgba(255,255,255,0))]" />

      {/* Container with max-width */}
      <div className='relative w-full max-w-[1920px] mx-auto flex'>
        {/* Left side - Decorative */}
        <div className='hidden lg:block relative w-1/2'>
          <div className='absolute inset-0 flex items-center justify-center p-8'>
            <div className='max-w-2xl text-center'>
              <div className='flex justify-center mb-8'>
                <img src={lufinCoin} alt='루핀 코인' className='w-32 h-32' />
              </div>
              <h3 className='text-h1 font-bold text-black'>금융 교육의 새로운 패러다임</h3>
              <p className='mt-4 text-h3 text-grey'>
                루핀과 함께 실전과 같은 금융 경험을 시작해보세요
              </p>
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

        {/* Right side - Login Form */}
        <div className='w-full lg:w-1/2 flex flex-col justify-center py-12 px-4 sm:px-6 lg:px-8 relative'>
          <div className='mx-auto w-full max-w-[480px]'>
            <div className='mt-10'>
              <div className='bg-white/80 backdrop-blur-md shadow-xl rounded-2xl p-10'>
                <div className='flex flex-col items-center mb-10'>
                  <img className='h-16 w-auto' src={logo} alt='루핀' />
                  <h2 className='mt-6 text-center text-h2 font-bold text-black'>
                    루핀과 함께하는 새로운 금융 교육
                  </h2>
                </div>

                <form className='space-y-6' onSubmit={handleSubmit}>
                  <TextField
                    label='이메일'
                    id='email'
                    name='email'
                    type='email'
                    value={email}
                    onChange={(e) => {
                      setEmail(e.target.value);
                      if (emailError) validateEmail(e.target.value);
                    }}
                    required
                    autoComplete='email'
                    placeholder='이메일을 입력해주세요'
                    inputSize='lg'
                    variant={emailError ? 'error' : 'normal'}
                    description={emailError}
                  />

                  <TextField
                    label='비밀번호'
                    id='password'
                    name='password'
                    type='password'
                    value={password}
                    onChange={(e) => {
                      setPassword(e.target.value);
                      if (passwordError) validatePassword(e.target.value);
                    }}
                    required
                    autoComplete='current-password'
                    placeholder='비밀번호를 입력해주세요'
                    inputSize='lg'
                    variant={passwordError ? 'error' : 'normal'}
                    description={passwordError}
                  />

                  {error && <div className='text-error text-p2 mt-2'>{error}</div>}

                  <div className='space-y-3 pt-3'>
                    <Button type='submit' color='primary' size='lg' full disabled={isLoading}>
                      {isLoading ? '로그인 중...' : '로그인'}
                    </Button>

                    <Button
                      type='button'
                      color='neutral'
                      variant='solid'
                      size='lg'
                      full
                      onClick={() => navigate('/register')}
                    >
                      회원가입
                    </Button>
                  </div>
                </form>

                <div className='mt-6 text-center'>
                  <a href='#' className='text-p2 text-grey hover:text-info'>
                    비밀번호를 잊으셨나요?
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
