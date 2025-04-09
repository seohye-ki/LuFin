import { useEffect, useRef } from 'react';
import TextField from '../../../../components/Form/TextField';
import StepButtons from './StepButtons';
import useDebounce from '../hooks/useDebounce';

interface PasswordSetupProps {
  password: string;
  confirmPassword: string;
  validation: {
    password: { isValid: boolean; message: string };
  };
  onChange: (
    field: 'password' | 'confirmPassword',
  ) => (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: (
    field: 'password' | 'confirmPassword',
  ) => (e: React.FocusEvent<HTMLInputElement>) => void;
  onPrev: () => void;
  onNext: () => void;
}

export default function PasswordSetup({
  password,
  confirmPassword,
  validation,
  onChange,
  onBlur,
  onPrev,
  onNext,
}: PasswordSetupProps) {
  // 비밀번호 필드 디바운싱 처리
  const debouncedPassword = useDebounce(password, 1000);
  const debouncedConfirmPassword = useDebounce(confirmPassword, 1000);
  
  // 이전 값을 저장하는 ref
  const prevPasswordRef = useRef<string>('');
  const prevConfirmPasswordRef = useRef<string>('');

  // 디바운스된 비밀번호 값이 변경될 때마다 검증
  useEffect(() => {
    if (debouncedPassword && debouncedPassword !== prevPasswordRef.current) {
      prevPasswordRef.current = debouncedPassword;
      const event = { target: { value: debouncedPassword } } as React.FocusEvent<HTMLInputElement>;
      onBlur('password')(event);
    }
  }, [debouncedPassword, onBlur]);

  // 디바운스된 비밀번호 확인 값이 변경될 때마다 검증
  useEffect(() => {
    if (debouncedConfirmPassword && debouncedConfirmPassword !== prevConfirmPasswordRef.current) {
      prevConfirmPasswordRef.current = debouncedConfirmPassword;
      const event = { target: { value: debouncedConfirmPassword } } as React.FocusEvent<HTMLInputElement>;
      onBlur('confirmPassword')(event);
    }
  }, [debouncedConfirmPassword, onBlur]);

  return (
    <div className='space-y-4 transition-all duration-300'>
      <div className='space-y-4 pb-10'>
        <TextField
          label='비밀번호'
          id='password'
          name='password'
          type='password'
          value={password}
          onChange={onChange('password')}
          required
          placeholder='비밀번호를 입력해주세요'
          variant={password ? (validation.password.isValid ? 'success' : 'error') : 'normal'}
          description={!confirmPassword ? validation.password.message : ''}
          inputSize='lg'
        />
        <TextField
          label='비밀번호 확인'
          id='confirmPassword'
          name='confirmPassword'
          type='password'
          value={confirmPassword}
          onChange={onChange('confirmPassword')}
          required
          placeholder='비밀번호를 다시 입력해주세요'
          variant={
            confirmPassword ? (password === confirmPassword ? 'success' : 'error') : 'normal'
          }
          description={
            confirmPassword
              ? password === confirmPassword
                ? '비밀번호가 일치합니다'
                : '비밀번호가 일치하지 않습니다'
              : ''
          }
          inputSize='lg'
        />
      </div>
      <StepButtons onPrev={onPrev} onNext={onNext} isNextDisabled={!validation.password.isValid} />
    </div>
  );
}
