import TextField from '../../../../components/Form/TextField';
import StepButtons from './StepButtons';

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
  return (
    <div className="space-y-4 transition-all duration-300">
      <TextField
        label="이름"
        id="name"
        name="name"
        type="text"
        value={name}
        onChange={onChange('name')}
        onBlur={onBlur('name')}
        required
        placeholder="이름을 입력해주세요"
        variant={name ? (validation.name.isValid ? 'success' : 'error') : 'normal'}
        description={name ? validation.name.message : ''}
        inputSize="lg"
      />
      <TextField
        label="이메일"
        id="email"
        name="email"
        type="email"
        value={email}
        onChange={onChange('email')}
        onBlur={onBlur('email')}
        required
        autoComplete="email"
        placeholder="이메일을 입력해주세요"
        variant={email ? (validation.email.isValid ? 'success' : 'error') : 'normal'}
        description={email ? validation.email.message : ''}
        inputSize="lg"
      />
      <StepButtons
        onPrev={onPrev}
        onNext={onNext}
        isNextDisabled={!validation.name.isValid || !validation.email.isValid}
      />
    </div>
  );
} 