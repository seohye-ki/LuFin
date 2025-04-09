import { useEffect, useRef } from 'react';
import TextField from '../../../../components/Form/TextField';
import StepButtons from './StepButtons';
import useDebounce from '../hooks/useDebounce';

interface BasicInfoProps {
  name: string;
  email: string;
  validation: {
    name: { isValid: boolean; message: string };
    email: { isValid: boolean; message: string };
  };
  onChange: (field: 'name' | 'email') => (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: (field: 'name' | 'email') => (e: React.FocusEvent<HTMLInputElement>) => void;
  onPrev: () => void;
  onNext: () => void;
}

export default function BasicInfo({
  name,
  email,
  validation,
  onChange,
  onBlur,
  onPrev,
  onNext,
}: BasicInfoProps) {
  // 이메일 디바운싱 처리
  const debouncedEmail = useDebounce(email, 1000);
  // 이전 이메일 값을 저장하는 ref
  const prevEmailRef = useRef<string>('');

  // 디바운스된 이메일 값이 변경될 때마다 검증
  useEffect(() => {
    // 이메일 값이 있고, 이전 값과 다른 경우에만 검증 실행
    if (debouncedEmail && debouncedEmail !== prevEmailRef.current) {
      prevEmailRef.current = debouncedEmail;
      
      // 이메일 형식이 유효한지 간단히 확인 (정규식)
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (emailRegex.test(debouncedEmail)) {
        const event = { target: { value: debouncedEmail } } as React.FocusEvent<HTMLInputElement>;
        onBlur('email')(event);
      }
    }
  }, [debouncedEmail, onBlur]);

  return (
    <div className='space-y-4 transition-all duration-300'>
      <div className='space-y-4 pb-10'>
        <TextField
          label='이름'
          id='name'
          name='name'
          type='text'
          value={name}
          onChange={onChange('name')}
          onBlur={onBlur('name')}
          required
          placeholder='이름을 입력해주세요'
          variant={name ? (validation.name.isValid ? 'success' : 'error') : 'normal'}
          description={name ? validation.name.message : ''}
          inputSize='lg'
        />
        <TextField
          label='이메일'
          id='email'
          name='email'
          type='email'
          value={email}
          onChange={onChange('email')}
          required
          autoComplete='email'
          placeholder='이메일을 입력해주세요'
          variant={email ? (validation.email.isValid ? 'success' : 'error') : 'normal'}
          description={email ? validation.email.message : ''}
          inputSize='lg'
        />
      </div>
      <StepButtons
        onPrev={onPrev}
        onNext={onNext}
        isNextDisabled={!validation.name.isValid || !validation.email.isValid}
      />
    </div>
  );
}
