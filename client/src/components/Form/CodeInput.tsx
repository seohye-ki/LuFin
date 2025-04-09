import { useEffect, useRef, useState } from 'react';

interface CodeInputProps {
  length?: number;
  value?: string;
  onChange?: (value: string) => void;
  size?: 'sm' | 'md' | 'lg' | 'xl';
  error?: string;
  description?: string;
  isError?: boolean;
  isDisabled?: boolean;
  className?: string;
}

const CodeInput = ({
  length = 5,
  value = '',
  onChange,
  size = 'md',
  error,
  description,
  isError = false,
  isDisabled = false,
  className = '',
}: CodeInputProps) => {
  const [code, setCode] = useState<string[]>(Array(length).fill(''));
  const inputRefs = useRef<(HTMLInputElement | null)[]>([]);

  // 입력값이 외부에서 변경될 때 처리
  useEffect(() => {
    const newValue = value.toUpperCase().slice(0, length).split('');
    setCode(newValue.concat(Array(length - newValue.length).fill('')));
  }, [value, length]);

  // 크기별 스타일 설정
  const sizeStyles = {
    sm: 'w-8 h-8 text-h3',
    md: 'w-10 h-10 text-h2',
    lg: 'w-12 h-12 text-h1',
    xl: 'w-14 h-14 text-h1',
  };

  // 입력 처리
  const handleChange = (index: number, value: string) => {
    // 숫자와 영문 대문자만 허용하는 정규식
    const validInput = /^[0-9A-Z]$/;
    const upperValue = value.toUpperCase();

    if (value === '' || validInput.test(upperValue)) {
      const newCode = [...code];
      newCode[index] = upperValue;
      setCode(newCode);
      onChange?.(newCode.join(''));

      // 값이 입력되면 다음 입력으로 포커스 이동
      if (value !== '' && index < length - 1) {
        inputRefs.current[index + 1]?.focus();
      }
    }
  };

  // 키보드 이벤트 처리
  const handleKeyDown = (index: number, e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Backspace' && !code[index] && index > 0) {
      // 현재 입력이 비어있고 Backspace를 누르면 이전 입력으로 이동
      inputRefs.current[index - 1]?.focus();
    } else if (e.key === 'ArrowLeft' && index > 0) {
      // 왼쪽 화살표 키로 이전 입력으로 이동
      inputRefs.current[index - 1]?.focus();
    } else if (e.key === 'ArrowRight' && index < length - 1) {
      // 오른쪽 화살표 키로 다음 입력으로 이동
      inputRefs.current[index + 1]?.focus();
    }
  };

  // 붙여넣기 처리
  const handlePaste = (e: React.ClipboardEvent) => {
    e.preventDefault();
    const pastedData = e.clipboardData.getData('text').toUpperCase();
    const validChars = pastedData.match(/[0-9A-Z]/g) || [];
    const newCode = [...code];

    validChars.forEach((char, index) => {
      if (index < length) {
        newCode[index] = char;
      }
    });

    setCode(newCode);
    onChange?.(newCode.join(''));

    // 붙여넣기 후 마지막 입력으로 포커스 이동
    const focusIndex = Math.min(validChars.length, length - 1);
    inputRefs.current[focusIndex]?.focus();
  };

  // 에러 상태 확인: error가 있거나 isError가 true인 경우
  const hasError = !!error || isError;

  return (
    <div className={`flex flex-col gap-2 ${className}`}>
      <div className='flex gap-2'>
        {Array(length)
          .fill(0)
          .map((_, index) => (
            <input
              key={index}
              ref={(el: HTMLInputElement | null) => {
                inputRefs.current[index] = el;
              }}
              type='text'
              maxLength={1}
              value={code[index]}
              onChange={(e) => handleChange(index, e.target.value)}
              onKeyDown={(e) => handleKeyDown(index, e)}
              onPaste={handlePaste}
              disabled={isDisabled}
              className={`
              ${sizeStyles[size]}
              text-center rounded-lg font-semibold
              ${
                hasError
                  ? 'border-2 border-danger focus:border-danger focus:ring-danger/30'
                  : 'border-2 border-info focus:border-info focus:ring-info/30'
              }
              ${
                isDisabled
                  ? 'bg-broken-white text-grey cursor-not-allowed'
                  : 'bg-white text-info hover:border-info/80'
              }
              outline-none
              focus:ring-4 transition-all
            `}
            />
          ))}
      </div>
      {error && <p className='text-p2 text-danger text-center'>{error}</p>}
      {!error && description && (
        <p className={`text-p2 text-center ${isError ? 'text-danger' : 'text-dark-grey'}`}>
          {description}
        </p>
      )}
    </div>
  );
};

export type { CodeInputProps };
export default CodeInput;
