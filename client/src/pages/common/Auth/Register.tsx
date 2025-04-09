import { useState } from 'react';
import { Link } from 'react-router-dom';
import logo from '../../../assets/svgs/logo.svg';
import UserTypeSelect from './components/UserTypeSelect';
import BasicInfo from './components/BasicInfo';
import PasswordSetup from './components/PasswordSetup';
import AccountSetup from './components/AccountSetup';
import Completion from './components/Completion';
import ProgressBar from './components/ProgressBar';
import { useRegisterForm } from './hooks/useRegisterForm';
import { useRegisterStep } from './hooks/useRegisterStep';
import AuthInfoCard from './components/AuthInfoCard';
import { paths } from '../../../routes/paths';

export default function Register() {
  const {
    formData,
    validation,
    handleChange,
    handleAutoComplete,
    setUserType,
    handleSubmit,
    validateEmail,
  } = useRegisterForm();
  const { currentStep, totalSteps, stepTitles, handleNext, handlePrev, goToCompletion } =
    useRegisterStep();
  const [error, setError] = useState<string | null>(null);

  const handleBasicInfoNext = async () => {
    try {
      const isEmailValid = await validateEmail(formData.email);
      if (isEmailValid) {
        setTimeout(() => {
          handleNext();
        }, 0);
      }
    } catch {
      setError('이메일 확인 중 오류가 발생했습니다.');
    }
  };

  const handleAccountSetupNext = async () => {
    try {
      const result = await handleSubmit();
      if (result.success) {
        goToCompletion();
      } else {
        setError(result.message || '회원가입 중 오류가 발생했습니다');
      }
    } catch {
      setError('회원가입 처리 중 오류가 발생했습니다');
    }
  };

  return (
    <div className='relative flex min-h-screen bg-gradient-to-br from-light-cyan-30 via-info-30 to-yellow-30'>
      <div className="absolute inset-0 bg-[url('/grid.svg')] bg-center [mask-image:linear-gradient(180deg,white,rgba(255,255,255,0))]" />

      {/* Container with max-width */}
      <div className='relative w-full max-w-[1920px] mx-auto flex'>
        {/* 왼쪽 정보 카드 */}
        <AuthInfoCard />

        {/* Right side - Register Form */}
        <div className='w-full lg:w-1/2 flex flex-col justify-center py-12 px-4 sm:px-6 lg:px-8 relative'>
          <div className='mx-auto w-full max-w-[480px]'>
            <div>
              <div className='bg-white/80 backdrop-blur-md shadow-xl rounded-2xl p-10'>
                <div className='flex flex-col items-center mb-10'>
                  <Link to={paths.HOME}>
                    <img className='h-16 w-auto cursor-pointer' src={logo} alt='루핀' />
                  </Link>
                  <h2 className='mt-6 text-center text-h2 font-bold text-black'>회원가입</h2>
                </div>

                <ProgressBar
                  currentStep={currentStep}
                  totalSteps={totalSteps}
                  stepTitles={stepTitles}
                />

                {error && (
                  <div className='mb-4 p-4 bg-red-50 border border-red-200 rounded-lg text-red-600'>
                    {error}
                  </div>
                )}

                <div className='space-y-6'>
                  {currentStep === 1 && (
                    <UserTypeSelect
                      userType={formData.userType}
                      onUserTypeSelect={setUserType}
                      onNext={handleNext}
                    />
                  )}

                  {currentStep === 2 && (
                    <BasicInfo
                      name={formData.name}
                      email={formData.email}
                      validation={validation}
                      onChange={handleChange}
                      onBlur={handleAutoComplete}
                      onPrev={handlePrev}
                      onNext={handleBasicInfoNext}
                    />
                  )}

                  {currentStep === 3 && (
                    <PasswordSetup
                      password={formData.password}
                      confirmPassword={formData.confirmPassword}
                      validation={validation}
                      onChange={handleChange}
                      onBlur={handleAutoComplete}
                      onPrev={handlePrev}
                      onNext={handleNext}
                    />
                  )}

                  {currentStep === 4 && (
                    <AccountSetup
                      accountPassword={formData.accountPassword}
                      confirmAccountPassword={formData.confirmAccountPassword}
                      validation={validation}
                      onChange={handleChange}
                      onBlur={handleAutoComplete}
                      onPrev={handlePrev}
                      onNext={handleAccountSetupNext}
                    />
                  )}

                  {currentStep === 5 && <Completion userType={formData.userType} />}

                  {currentStep !== 5 && (
                    <div className='mt-6 text-center'>
                      <button
                        type='button'
                        className='text-p2 text-grey hover:text-info'
                        onClick={() => (window.location.href = '/login')}
                      >
                        이미 계정이 있으신가요?
                      </button>
                    </div>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
