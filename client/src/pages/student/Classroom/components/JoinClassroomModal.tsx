import { useState } from 'react';
import { Icon } from '../../../../components/Icon/Icon';
import Button from '../../../../components/Button/Button';
import CodeInput from '../../../../components/Form/CodeInput';

interface JoinClassroomModalProps {
  onClose: () => void;
  onSubmit: (code: string) => void;
  isLoading?: boolean;
}

const JoinClassroomModal = ({ onClose, onSubmit, isLoading = false }: JoinClassroomModalProps) => {
  const [code, setCode] = useState('');
  const [error, setError] = useState<string | undefined>(undefined);

  const handleSubmit = () => {
    if (code.length < 5) {
      setError('코드를 모두 입력해주세요.');
      return;
    }

    // 부모 컴포넌트에 코드 제출
    onSubmit(code);
  };

  return (
    <div className='fixed inset-0 bg-black/30 flex items-center justify-center z-50'>
      <div className='bg-white rounded-2xl w-full max-w-md p-6 shadow-lg flex flex-col gap-6 relative'>
        <button
          onClick={onClose}
          className='absolute top-4 right-4 flex items-center justify-center'
        >
          <Icon name='Close' size={32} color='grey' />
        </button>

        <div className='flex flex-col items-center gap-4 mt-2'>
          <Icon name='InfoCircle' variant='Bold' size={42} color='warning' />
          <div className='flex flex-col items-center gap-1'>
            <h2 className='text-h2 font-semibold'>새로운 반 입장하기</h2>
            <p className='text-p1 text-dark-grey'>클래스 코드를 입력해주세요</p>
          </div>
        </div>

        <div className='flex flex-col items-center gap-4'>
          <CodeInput
            length={5}
            value={code}
            onChange={(value) => {
              setCode(value);
              if (error) setError(undefined);
            }}
            size='lg'
            error={error}
            className='mt-2'
          />
        </div>
        <div className='flex gap-3 mt-6'>
          <Button color='neutral' variant='solid' size='md' onClick={onClose} full>
            취소하기
          </Button>
          <Button
            color={code.length === 5 ? (isLoading ? 'disabled' : 'primary') : 'disabled'}
            variant='solid'
            size='md'
            onClick={handleSubmit}
            full
            disabled={isLoading}
          >
            {isLoading ? '처리 중...' : '입장하기'}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default JoinClassroomModal;
