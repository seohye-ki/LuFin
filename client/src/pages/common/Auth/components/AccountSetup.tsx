import TextField from '../../../../components/Form/TextField';
import StepButtons from './StepButtons';

interface AccountSetupProps {
  accountPassword: string;
  confirmAccountPassword: string;
  validation: {
    accountPassword: { isValid: boolean; message: string };
  };
  onChange: (
    field: 'accountPassword' | 'confirmAccountPassword',
  ) => (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: (
    field: 'accountPassword' | 'confirmAccountPassword',
  ) => (e: React.FocusEvent<HTMLInputElement>) => void;
  onPrev: () => void;
  onNext: () => void;
}

export default function AccountSetup({
  accountPassword,
  confirmAccountPassword,
  validation,
  onChange,
  onBlur,
  onPrev,
  onNext,
}: AccountSetupProps) {
  return (
    <div className='space-y-4 transition-all duration-300'>
      <TextField
        label='계좌비밀번호'
        id='accountPassword'
        name='accountPassword'
        type='password'
        value={accountPassword}
        onChange={onChange('accountPassword')}
        onBlur={onBlur('accountPassword')}
        required
        maxLength={6}
        placeholder='6자리 숫자를 입력해주세요'
        variant={
          accountPassword ? (/^\d{6}$/.test(accountPassword) ? 'success' : 'error') : 'normal'
        }
        description={
          accountPassword
            ? /^\d{6}$/.test(accountPassword)
              ? '사용 가능한 계좌비밀번호입니다'
              : '계좌비밀번호는 6자리 숫자여야 합니다'
            : ''
        }
        inputSize='lg'
      />
      <TextField
        label='계좌비밀번호 확인'
        id='confirmAccountPassword'
        name='confirmAccountPassword'
        type='password'
        value={confirmAccountPassword}
        onChange={onChange('confirmAccountPassword')}
        onBlur={onBlur('confirmAccountPassword')}
        required
        maxLength={6}
        placeholder='계좌비밀번호를 다시 입력해주세요'
        variant={
          confirmAccountPassword
            ? accountPassword === confirmAccountPassword
              ? 'success'
              : 'error'
            : 'normal'
        }
        description={
          confirmAccountPassword
            ? accountPassword === confirmAccountPassword
              ? '계좌비밀번호가 일치합니다'
              : '계좌비밀번호가 일치하지 않습니다'
            : ''
        }
        inputSize='lg'
      />
      <StepButtons
        onPrev={onPrev}
        onNext={onNext}
        isNextDisabled={!validation.accountPassword.isValid}
      />
    </div>
  );
}
