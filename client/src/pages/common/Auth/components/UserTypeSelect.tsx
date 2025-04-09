import StepButtons from './StepButtons';
import studentImage from '../../../../assets/images/student.png';
import teacherImage from '../../../../assets/images/teacher.png';

interface UserTypeSelectProps {
  userType: 'teacher' | 'student' | null;
  onUserTypeSelect: (type: 'teacher' | 'student') => void;
  onNext: () => void;
}

export default function UserTypeSelect({
  userType,
  onUserTypeSelect,
  onNext,
}: UserTypeSelectProps) {
  return (
    <div className='space-y-6 transition-all duration-300'>
      <div className='grid grid-cols-2 gap-6 pb-10'>
        <button
          type='button'
          className={`p-6 rounded-xl border-2 transition-all duration-200 flex flex-col items-center ${
            userType === 'teacher'
              ? 'border-info bg-info/10 text-info'
              : 'border-grey-30 hover:border-info/50'
          }`}
          onClick={() => onUserTypeSelect('teacher')}
        >
          <img src={teacherImage} alt='선생님' className='w-32 h-32 mb-4' />
          <p className='text-h3 font-bold'>선생님</p>
        </button>
        <button
          type='button'
          className={`p-6 rounded-xl border-2 transition-all duration-200 flex flex-col items-center ${
            userType === 'student'
              ? 'border-info bg-info/10 text-info'
              : 'border-grey-30 hover:border-info/50'
          }`}
          onClick={() => onUserTypeSelect('student')}
        >
          <img src={studentImage} alt='학생' className='w-32 h-32 mb-4' />
          <p className='text-h3 font-bold'>학생</p>
        </button>
      </div>
      <StepButtons onNext={onNext} isNextDisabled={!userType} showPrev={false} />
    </div>
  );
}
