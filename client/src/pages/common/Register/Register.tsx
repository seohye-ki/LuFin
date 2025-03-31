import { useState } from 'react';
import Button from '../../../components/Button/Button';
import TextField from '../../../components/Form/TextField';
import logo from '../../../assets/svgs/logo.svg';
import lufinCoin from '../../../assets/svgs/lufin-coin-200.svg';

interface FormState {
  userType: 'teacher' | 'student' | null;
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
  accountPassword: string;
  confirmAccountPassword: string;
}

interface ValidationState {
  isUserTypeSelected: boolean;
  name: {
    isValid: boolean;
    message: string;
  };
  email: {
    isValid: boolean;
    message: string;
  };
  password: {
    isValid: boolean;
    message: string;
  };
  accountPassword: {
    isValid: boolean;
    message: string;
  };
}

export default function Register() {
  const [formData, setFormData] = useState<FormState>({
    userType: null,
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    accountPassword: '',
    confirmAccountPassword: '',
  });

  const [validation, setValidation] = useState<ValidationState>({
    isUserTypeSelected: false,
    name: {
      isValid: false,
      message: '',
    },
    email: {
      isValid: false,
      message: '',
    },
    password: {
      isValid: false,
      message: '',
    },
    accountPassword: {
      isValid: false,
      message: '',
    },
  });

  // 현재 단계를 추적하는 상태 추가
  const [currentStep, setCurrentStep] = useState(1);
  
  // 전체 단계 정의
  const totalSteps = 5;
  const stepTitles = ['역할 선택', '기본 정보', '비밀번호 설정', '계좌 설정', '가입 완료'];

  // 현재 진행률 계산
  const progress = (currentStep / totalSteps) * 100;

  // 다음 단계로 이동
  const handleNext = () => {
    if (currentStep < totalSteps) {
      setCurrentStep(prev => prev + 1);
    }
  };

  // 이전 단계로 이동
  const handlePrev = () => {
    if (currentStep > 1) {
      setCurrentStep(prev => prev - 1);
    }
  };

  // 이메일 유효성 검사
  const validateEmail = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isValid = emailRegex.test(email);
    setValidation(prev => ({
      ...prev,
      email: {
        isValid,
        message: isValid ? '사용 가능한 이메일입니다' : '올바른 이메일 형식이 아닙니다',
      },
    }));
    return isValid;
  };

  // 비밀번호 유효성 검사
  const validatePassword = () => {
    const isLengthValid = formData.password.length >= 8;
    const isMatching = formData.password === formData.confirmPassword;
    const isValid = isLengthValid && isMatching;

    setValidation(prev => ({
      ...prev,
      password: {
        isValid,
        message: isMatching ? '비밀번호가 일치합니다' : '비밀번호가 일치하지 않습니다',
      },
    }));
    return isValid;
  };

  // 이름 유효성 검사
  const validateName = (name: string) => {
    const isValid = name.length >= 2;
    setValidation(prev => ({
      ...prev,
      name: {
        isValid,
        message: isValid ? '사용 가능한 이름입니다' : '이름은 2자 이상이어야 합니다',
      },
    }));
    return isValid;
  };

  // 계좌 비밀번호 유효성 검사
  const validateAccountPassword = () => {
    const isValid = /^\d{6}$/.test(formData.accountPassword) && 
                   formData.accountPassword === formData.confirmAccountPassword;
    
    let message = '';
    if (!/^\d{6}$/.test(formData.accountPassword) && formData.accountPassword) {
      message = '계좌비밀번호는 6자리 숫자여야 합니다';
    } else if (/^\d{6}$/.test(formData.accountPassword) && 
               formData.accountPassword !== formData.confirmAccountPassword && 
               formData.confirmAccountPassword) {
      message = '계좌비밀번호가 일치하지 않습니다';
    } else if (isValid && formData.confirmAccountPassword) {
      message = '사용 가능한 계좌비밀번호입니다';
    }

    setValidation(prev => ({
      ...prev,
      accountPassword: {
        isValid,
        message,
      },
    }));
    return isValid;
  };

  // 입력값 변경 핸들러
  const handleChange = (field: keyof FormState) => (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const newValue = e.target.value;
    
    // 계좌비밀번호의 경우 숫자만 입력 가능하도록 처리
    if (field === 'accountPassword' && !/^\d*$/.test(newValue)) {
      return;
    }
    
    setFormData(prev => ({ ...prev, [field]: newValue }));

    // 유효성 검사 업데이트
    if (field === 'email') {
      validateEmail(newValue);
    } else if (field === 'name') {
      validateName(newValue);
    } else if (field === 'password' || field === 'confirmPassword') {
      validatePassword();
    } else if (field === 'accountPassword' || field === 'confirmAccountPassword') {
      validateAccountPassword();
    }
  };

  // 자동완성 처리를 위한 이벤트 핸들러
  const handleAutoComplete = (field: keyof FormState) => (
    e: React.FocusEvent<HTMLInputElement>
  ) => {
    const value = e.target.value;
    if (value) {
      if (field === 'email') {
        validateEmail(value);
      } else if (field === 'name') {
        validateName(value);
      } else if (field === 'password' || field === 'confirmPassword') {
        validatePassword();
      } else if (field === 'accountPassword' || field === 'confirmAccountPassword') {
        validateAccountPassword();
      }
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle register logic here
    setCurrentStep(5); // 회원가입 성공 시 마지막 단계로 이동
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

      {/* Right side - Register Form */}
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
                  회원가입
                </h2>
              </div>

              {/* 프로그레스 바 및 단계 표시 */}
              <div className="mb-8">
                <div className="flex justify-between mb-2">
                  {stepTitles.map((title, index) => (
                    <div
                      key={title}
                      className={`text-sm font-medium ${
                        index + 1 === currentStep ? 'text-info' : 'text-grey'
                      }`}
                    >
                      {title}
                    </div>
                  ))}
                </div>
                <div className="h-2 bg-grey-30 rounded-full">
                  <div
                    className="h-full bg-info rounded-full transition-all duration-300"
                    style={{ width: `${progress}%` }}
                  />
                </div>
              </div>

              <form className="space-y-5" onSubmit={handleSubmit}>
                {/* Step 1: 역할 선택 */}
                {currentStep === 1 && (
                  <div className="space-y-4 transition-all duration-300">
                    <label className="block text-sm font-medium text-grey">역할</label>
                    <div className="grid grid-cols-2 gap-4">
                      <button
                        type="button"
                        className={`p-4 rounded-xl border-2 transition-all duration-200 ${
                          formData.userType === 'teacher'
                            ? 'border-info bg-info/10 text-info'
                            : 'border-grey-30 hover:border-info/50'
                        }`}
                        onClick={() => {
                          setFormData(prev => ({ ...prev, userType: 'teacher' }));
                          setValidation(prev => ({ ...prev, isUserTypeSelected: true }));
                        }}
                      >
                        <p className="font-bold">저는 선생님이에요!</p>
                        <p className="text-sm text-grey mt-1">
                          우리반 학생들을<br />관리할 수 있어요
                        </p>
                      </button>
                      <button
                        type="button"
                        className={`p-4 rounded-xl border-2 transition-all duration-200 ${
                          formData.userType === 'student'
                            ? 'border-info bg-info/10 text-info'
                            : 'border-grey-30 hover:border-info/50'
                        }`}
                        onClick={() => {
                          setFormData(prev => ({ ...prev, userType: 'student' }));
                          setValidation(prev => ({ ...prev, isUserTypeSelected: true }));
                        }}
                      >
                        <p className="font-bold">저는 학생이에요!</p>
                        <p className="text-sm text-grey mt-1">
                          금융 교육을 배우고<br />실천할 수 있어요
                        </p>
                      </button>
                    </div>
                    <div className="flex justify-end mt-4">
                      <Button
                        type="button"
                        color="primary"
                        size="md"
                        disabled={!formData.userType}
                        onClick={handleNext}
                      >
                        다음
                      </Button>
                    </div>
                  </div>
                )}

                {/* Step 2: 기본 정보 */}
                {currentStep === 2 && (
                  <div className="space-y-4 transition-all duration-300">
                    <TextField
                      label="이름"
                      id="name"
                      name="name"
                      type="text"
                      value={formData.name}
                      onChange={handleChange('name')}
                      onBlur={handleAutoComplete('name')}
                      required
                      placeholder="이름을 입력해주세요"
                      variant={formData.name ? (validation.name.isValid ? 'success' : 'error') : 'normal'}
                      description={formData.name ? validation.name.message : ''}
                    />
                    <TextField
                      label="이메일"
                      id="email"
                      name="email"
                      type="email"
                      value={formData.email}
                      onChange={handleChange('email')}
                      onBlur={handleAutoComplete('email')}
                      required
                      autoComplete="email"
                      placeholder="이메일을 입력해주세요"
                      variant={formData.email ? (validation.email.isValid ? 'success' : 'error') : 'normal'}
                      description={formData.email ? validation.email.message : ''}
                    />
                    <div className="flex justify-between mt-4">
                      <Button
                        type="button"
                        color="neutral"
                        size="md"
                        onClick={handlePrev}
                      >
                        이전
                      </Button>
                      <Button
                        type="button"
                        color="primary"
                        size="md"
                        disabled={!validation.name.isValid || !validation.email.isValid}
                        onClick={handleNext}
                      >
                        다음
                      </Button>
                    </div>
                  </div>
                )}

                {/* Step 3: 비밀번호 설정 */}
                {currentStep === 3 && (
                  <div className="space-y-4 transition-all duration-300">
                    <TextField
                      label="비밀번호"
                      id="password"
                      name="password"
                      type="password"
                      value={formData.password}
                      onChange={handleChange('password')}
                      onBlur={handleAutoComplete('password')}
                      required
                      placeholder="비밀번호를 입력해주세요"
                      variant={formData.password 
                        ? (formData.password.length >= 8 ? 'success' : 'error')
                        : 'normal'}
                      description={formData.password 
                        ? (formData.password.length >= 8 
                          ? '사용 가능한 비밀번호입니다' 
                          : '비밀번호는 8자 이상이어야 합니다')
                        : ''}
                    />
                    <TextField
                      label="비밀번호 확인"
                      id="confirmPassword"
                      name="confirmPassword"
                      type="password"
                      value={formData.confirmPassword}
                      onChange={handleChange('confirmPassword')}
                      onBlur={handleAutoComplete('confirmPassword')}
                      required
                      placeholder="비밀번호를 다시 입력해주세요"
                      variant={formData.confirmPassword
                        ? (formData.password === formData.confirmPassword ? 'success' : 'error')
                        : 'normal'}
                      description={formData.confirmPassword
                        ? (formData.password === formData.confirmPassword
                          ? '비밀번호가 일치합니다'
                          : '비밀번호가 일치하지 않습니다')
                        : ''}
                    />
                    <div className="flex justify-between mt-4">
                      <Button
                        type="button"
                        color="neutral"
                        size="md"
                        onClick={handlePrev}
                      >
                        이전
                      </Button>
                      <Button
                        type="button"
                        color="primary"
                        size="md"
                        disabled={!validation.password.isValid}
                        onClick={handleNext}
                      >
                        다음
                      </Button>
                    </div>
                  </div>
                )}

                {/* Step 4: 계좌 설정 */}
                {currentStep === 4 && (
                  <div className="space-y-4 transition-all duration-300">
                    <TextField
                      label="계좌비밀번호"
                      id="accountPassword"
                      name="accountPassword"
                      type="password"
                      value={formData.accountPassword}
                      onChange={handleChange('accountPassword')}
                      onBlur={handleAutoComplete('accountPassword')}
                      required
                      maxLength={6}
                      placeholder="6자리 숫자를 입력해주세요"
                      variant={formData.accountPassword 
                        ? (/^\d{6}$/.test(formData.accountPassword) ? 'success' : 'error')
                        : 'normal'}
                      description={formData.accountPassword 
                        ? (/^\d{6}$/.test(formData.accountPassword)
                          ? '사용 가능한 계좌비밀번호입니다' 
                          : '계좌비밀번호는 6자리 숫자여야 합니다')
                        : ''}
                    />
                    <TextField
                      label="계좌비밀번호 확인"
                      id="confirmAccountPassword"
                      name="confirmAccountPassword"
                      type="password"
                      value={formData.confirmAccountPassword}
                      onChange={handleChange('confirmAccountPassword')}
                      onBlur={handleAutoComplete('confirmAccountPassword')}
                      required
                      maxLength={6}
                      placeholder="계좌비밀번호를 다시 입력해주세요"
                      variant={formData.confirmAccountPassword
                        ? (formData.accountPassword === formData.confirmAccountPassword ? 'success' : 'error')
                        : 'normal'}
                      description={formData.confirmAccountPassword
                        ? (formData.accountPassword === formData.confirmAccountPassword
                          ? '계좌비밀번호가 일치합니다'
                          : '계좌비밀번호가 일치하지 않습니다')
                        : ''}
                    />
                    <div className="flex justify-between mt-4">
                      <Button
                        type="button"
                        color="neutral"
                        size="md"
                        onClick={handlePrev}
                      >
                        이전
                      </Button>
                      <Button
                        type="button"
                        color="primary"
                        size="md"
                        disabled={!validation.accountPassword.isValid}
                        onClick={handleNext}
                      >
                        다음
                      </Button>
                    </div>
                  </div>
                )}

                {/* Step 5: 회원가입 완료 */}
                {currentStep === 5 && (
                  <div className="space-y-8 transition-all duration-300">
                    <div className="flex flex-col items-center text-center">
                      <div className="w-32 h-32 mb-6">
                        <img src={lufinCoin} alt="루핀 코인" className="w-full h-full" />
                      </div>
                      <h3 className="text-h3 font-bold text-black mb-2">
                        {formData.userType === 'teacher' ? '선생님' : '학생'}으로 가입이 완료되었어요!
                      </h3>
                      <p className="text-p2 text-grey">
                        루핀과 함께 실전과 같은<br />
                        금융 교육을 시작해보세요
                      </p>
                    </div>
                    <div className="flex justify-center">
                      <Button
                        type="button"
                        color="primary"
                        size="lg"
                        full
                        onClick={() => window.location.href = '/login'}
                      >
                        로그인하러 가기
                      </Button>
                    </div>
                  </div>
                )}

                {currentStep !== 5 && (
                  <div className="mt-4 text-center">
                    <button
                      type="button"
                      className="text-p3 text-grey hover:text-info"
                      onClick={() => window.location.href = '/login'}
                    >
                      이미 계정이 있으신가요?
                    </button>
                  </div>
                )}
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
} 