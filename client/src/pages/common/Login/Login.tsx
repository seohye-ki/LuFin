import { useState } from 'react';
import Button from '../../../components/Button/Button';
import TextField from '../../../components/Form/TextField';
import logo from '../../../assets/svgs/logo.svg';
import lufinCoin from '../../../assets/svgs/lufin-coin-200.svg';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle login logic here
  };

  return (
    <div className="relative flex min-h-screen bg-gradient-to-br from-light-cyan-30 via-info-30 to-yellow-30">
      <div className="absolute inset-0 bg-[url('/grid.svg')] bg-center [mask-image:linear-gradient(180deg,white,rgba(255,255,255,0))]" />
      
      {/* Left side - Decorative */}
      <div className="hidden lg:block relative w-1/2">
        <div className="absolute inset-0 flex items-center justify-center p-8">
          <div className="max-w-2xl text-center">
            <div className="flex justify-center mb-8">
              <img src={lufinCoin} alt="루핀 코인" className="w-32 h-32" />
            </div>
            <h3 className="text-h1 font-bold text-black">
              금융 교육의 새로운 패러다임
            </h3>
            <p className="mt-4 text-h3 text-grey">
              루핀과 함께 실전과 같은 금융 경험을 시작해보세요
            </p>
            <div className="mt-8 grid grid-cols-1 gap-4 sm:grid-cols-3">
              <div className="rounded-2xl bg-white/10 backdrop-blur-sm p-4">
                <p className="text-p1 font-semibold text-black">신용등급 시스템</p>
              </div>
              <div className="rounded-2xl bg-white/10 backdrop-blur-sm p-4">
                <p className="text-p1 font-semibold text-black">AI 생성 투자 종목</p>
              </div>
              <div className="rounded-2xl bg-white/10 backdrop-blur-sm p-4">
                <p className="text-p1 font-semibold text-black">맞춤형 리포트</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Right side - Login Form */}
      <div className="w-full lg:w-1/2 flex flex-col justify-center py-12 px-4 sm:px-6 lg:px-8 relative">
        <div className="mx-auto w-full max-w-[420px]">
          <div className="mt-10">
            <div className="bg-white/80 backdrop-blur-md shadow-xl rounded-2xl p-8">
              <div className="flex flex-col items-center mb-8">
                <img
                  className="h-14 w-auto"
                  src={logo}
                  alt="루핀"
                />
                <h2 className="mt-5 text-center text-h3 font-bold text-black">
                  루핀과 함께하는 새로운 금융 교육
                </h2>
              </div>

              <form className="space-y-5" onSubmit={handleSubmit}>
                <TextField
                  label="이메일"
                  id="email"
                  name="email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  autoComplete="email"
                  placeholder="이메일을 입력해주세요"
                />

                <TextField
                  label="비밀번호"
                  id="password"
                  name="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  autoComplete="current-password"
                  placeholder="비밀번호를 입력해주세요"
                />

                <div className="space-y-2 pt-2">
                  <Button
                    type="submit"
                    color="primary"
                    size="md"
                    full
                  >
                    로그인
                  </Button>
                  
                  <Button
                    type="button"
                    color="neutral"
                    variant="solid"
                    size="md"
                    full
                    onClick={() => window.location.href = '/register'}
                  >
                    회원가입
                  </Button>
                </div>
              </form>

              <div className="mt-4 text-center">
                <a href="#" className="text-p3 text-grey hover:text-info">
                  비밀번호를 잊으셨나요?
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
} 