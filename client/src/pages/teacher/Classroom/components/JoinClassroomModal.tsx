import { useState } from 'react';
import CodeInput from '../../../../components/Form/CodeInput';
import { classroomService } from '../../../../libs/services/classroom/classroomService';
import Button from '../../../../components/Button/Button';

interface JoinClassroomModalProps {
  onClose: () => void;
}

const JoinClassroomModal = ({ onClose }: JoinClassroomModalProps) => {
  const [code, setCode] = useState('');

  const fetchJoinClass = async (code: string) => {
    await classroomService.joinClass(code);
  };

  const handleSubmit = () => {
    fetchJoinClass(code);
  };

  return (
    <div
      className='fixed inset-0 bg-black/30 flex items-center justify-center z-50'
      onClick={onClose}
    >
      <div
        className='w-fit h-fit p-8 rounded-2xl gap-8 bg-white flex flex-col items-center'
        onClick={(e) => e.stopPropagation()}
      >
        <h2 className='text-h2 font-bold mb-4'>클래스 코드 입력</h2>

        <CodeInput value={code} onChange={setCode} length={5} size='xl' />

        <div className='w-full flex flex-row gap-4'>
          <Button color='neutral' className='w-full' onClick={onClose}>
            취소하기
          </Button>
          <Button className='w-full' onClick={handleSubmit}>
            입장하기
          </Button>
        </div>
      </div>
    </div>
  );
};

export default JoinClassroomModal;
