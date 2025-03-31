import StepButtons from './StepButtons';

interface UserTypeSelectProps {
  userType: 'teacher' | 'student' | null;
  onUserTypeSelect: (type: 'teacher' | 'student') => void;
  onNext: () => void;
}

export default function UserTypeSelect({ userType, onUserTypeSelect, onNext }: UserTypeSelectProps) {
  return (
    <div className="space-y-6 transition-all duration-300">
      <label className="block text-p1 font-medium text-grey">역할</label>
      <div className="grid grid-cols-2 gap-6">
        <button
          type="button"
          className={`p-6 rounded-xl border-2 transition-all duration-200 ${
            userType === 'teacher'
              ? 'border-info bg-info/10 text-info'
              : 'border-grey-30 hover:border-info/50'
          }`}
          onClick={() => onUserTypeSelect('teacher')}
        >
          <p className="text-h3 font-bold">저는 선생님이에요!</p>
          <p className="text-p1 text-grey mt-2">
            우리반 학생들을<br />관리할 수 있어요
          </p>
        </button>
        <button
          type="button"
          className={`p-6 rounded-xl border-2 transition-all duration-200 ${
            userType === 'student'
              ? 'border-info bg-info/10 text-info'
              : 'border-grey-30 hover:border-info/50'
          }`}
          onClick={() => onUserTypeSelect('student')}
        >
          <p className="text-h3 font-bold">저는 학생이에요!</p>
          <p className="text-p1 text-grey mt-2">
            금융 교육을 배우고<br />실천할 수 있어요
          </p>
        </button>
      </div>
      <StepButtons
        onNext={onNext}
        isNextDisabled={!userType}
        showPrev={false}
      />
    </div>
  );
} 