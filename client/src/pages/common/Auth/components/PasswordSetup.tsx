import TextField from '../../../../components/Form/TextField';
import StepButtons from './StepButtons';

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
          onBlur={onBlur('password')}
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
          onBlur={onBlur('confirmPassword')}
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
