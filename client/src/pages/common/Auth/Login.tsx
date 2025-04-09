import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Button from '../../../components/Button/Button';
import TextField from '../../../components/Form/TextField';
import logo from '../../../assets/svgs/logo.svg';
import useAuthStore from '../../../libs/store/authStore';
import AuthInfoCard from './components/AuthInfoCard';
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

    const result = await login({ email, password });

    if (result.success && result.redirectPath) {
      navigate(result.redirectPath);
    }
  };

  return (
    <div className='relative flex min-h-screen bg-gradient-to-br from-light-cyan-30 via-info-30 to-yellow-30'>
      <div className="absolute inset-0 bg-[url('/grid.svg')] bg-center [mask-image:linear-gradient(180deg,white,rgba(255,255,255,0))]" />

      {/* Container with max-width */}
      <div className='relative w-full max-w-[1920px] mx-auto flex'>
        {/* 왼쪽 정보 카드 */}
        <AuthInfoCard />

        {/* Right side - Login Form */}
        <div className='w-full lg:w-1/2 flex flex-col justify-center py-12 px-4 sm:px-6 lg:px-8 relative'>
          <div className='mx-auto w-full max-w-[480px]'>
            <div>
              <div className='bg-white/80 backdrop-blur-md shadow-xl rounded-2xl p-10'>
                <div className='flex flex-col items-center mb-10'>
                  <Link to={paths.HOME}>
                    <img className='h-16 mb-4 w-auto cursor-pointer' src={logo} alt='루핀' />
                  </Link>
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
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
