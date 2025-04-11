import React, { useState } from 'react';
import Card from '../../../../components/Card/Card';
import Button from '../../../../components/Button/Button';
import Lufin from '../../../../components/Lufin/Lufin';

interface RecoveryApproveModalProps {
  studentName: string;
  creditGrade: string;
  currentAsset: number;
  loanAmount: number;
  investmentAmount: number;
  onClose: () => void;
  onApprove: () => void;
}

// 정보 표시를 위한 컴포넌트
interface InfoItemProps {
  label: string;
  children: React.ReactNode;
}

const InfoItem: React.FC<InfoItemProps> = ({ label, children }) => (
  <div className='flex-1'>
    <p className='text-p1 font-medium mb-2'>{label}</p>
    <Card className='bg-background px-4 py-3'>{children}</Card>
  </div>
);

// 체크박스 항목 컴포넌트
interface CheckboxItemProps {
  id: string;
  label: string;
  checked: boolean;
  onChange: () => void;
}

const CheckboxItem: React.FC<CheckboxItemProps> = ({ id, label, checked, onChange }) => (
  <div className='flex items-start gap-3'>
    <input
      type='checkbox'
      className='mt-1 w-4 h-4 accent-primary'
      id={id}
      checked={checked}
      onChange={onChange}
    />
    <label htmlFor={id} className='text-p1 text-grey-70 cursor-pointer'>
      {label}
    </label>
  </div>
);

const RecoveryApproveModal: React.FC<RecoveryApproveModalProps> = ({
  studentName,
  creditGrade,
  currentAsset,
  loanAmount,
  investmentAmount,
  onClose,
  onApprove,
}) => {
  const [checkboxes, setCheckboxes] = useState({
    check1: false,
    check2: false,
    check3: false,
    check4: false,
    check5: false,
  });

  const handleCheckAll = () => {
    const allChecked = Object.values(checkboxes).every((value) => value);
    setCheckboxes({
      check1: !allChecked,
      check2: !allChecked,
      check3: !allChecked,
      check4: !allChecked,
      check5: !allChecked,
    });
  };

  const handleSingleCheck = (checkboxId: keyof typeof checkboxes) => {
    setCheckboxes((prev) => ({
      ...prev,
      [checkboxId]: !prev[checkboxId],
    }));
  };

  const isAllChecked = Object.values(checkboxes).every((value) => value);

  // 체크박스 데이터
  const checkboxItems = [
    { id: 'check1', label: '학생과 상담을 진행했나요?' },
    { id: 'check2', label: '학생이 회생 계획서를 작성했나요?' },
    { id: 'check3', label: '학생이 회생 계획서를 이해하고 있나요?' },
    { id: 'check4', label: '학생이 회생 계획서를 실천할 의지가 있나요?' },
    { id: 'check5', label: '학생과 F+로 바뀌기 위한 일정을 잡았나요?' },
  ];

  return (
    <div
      className='fixed inset-0 bg-black/50 flex items-center justify-center z-50'
      onClick={onClose}
    >
      <div
        className='bg-white rounded-2xl p-6 w-[480px] max-h-[90vh] overflow-y-auto'
        onClick={(e) => e.stopPropagation()}
      >
        <h2 className='text-h3 font-bold mb-6'>회생 승인</h2>

        <div className='mb-6'>
          <p className='text-p font-medium mb-2'>이름</p>
          <Card className='bg-background px-4 py-3'>
            <p className='text-p1 font-semibold'>{studentName}</p>
          </Card>
        </div>

        <div className='flex gap-6 mb-6'>
          <InfoItem label='신용등급'>
            <p className={`text-p1 font-semibold ${creditGrade === 'F-' ? 'text-danger' : ''}`}>
              {creditGrade}
            </p>
          </InfoItem>

          <InfoItem label='자산'>
            <Lufin size='m' count={currentAsset} />
          </InfoItem>
        </div>

        <div className='flex gap-6 mb-8'>
          <InfoItem label='대출'>
            <Lufin size='m' count={loanAmount} />
          </InfoItem>

          <InfoItem label='투자'>
            <Lufin size='m' count={investmentAmount} />
          </InfoItem>
        </div>

        <h3 className='text-p1 font-semibold mb-4 text-grey-70'>회생 승인 확인사항</h3>
        <div className='flex flex-col gap-4 mb-8 bg-background p-4 rounded-md'>
          {checkboxItems.map((item) => (
            <CheckboxItem
              key={item.id}
              id={item.id}
              label={item.label}
              checked={checkboxes[item.id as keyof typeof checkboxes]}
              onChange={() => handleSingleCheck(item.id as keyof typeof checkboxes)}
            />
          ))}
        </div>

        <div className='flex flex-col gap-3'>
          <Button color='neutral' size='md' full onClick={handleCheckAll}>
            {isAllChecked ? '모두 해제' : '모두 동의'}
          </Button>
          <div className='flex gap-3'>
            <Button color='neutral' size='md' full onClick={onClose}>
              취소하기
            </Button>
            <Button
              color={isAllChecked ? 'primary' : 'disabled'}
              size='md'
              full
              onClick={onApprove}
              disabled={!isAllChecked}
            >
              승인하기
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RecoveryApproveModal;
