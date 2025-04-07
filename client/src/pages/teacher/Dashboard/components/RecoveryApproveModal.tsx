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
    const allChecked = Object.values(checkboxes).every(value => value);
    setCheckboxes({
      check1: !allChecked,
      check2: !allChecked,
      check3: !allChecked,
      check4: !allChecked,
      check5: !allChecked,
    });
  };

  const handleSingleCheck = (checkboxId: keyof typeof checkboxes) => {
    setCheckboxes(prev => ({
      ...prev,
      [checkboxId]: !prev[checkboxId]
    }));
  };

  const isAllChecked = Object.values(checkboxes).every(value => value);

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50" onClick={onClose}>
      <div className="bg-white rounded-2xl p-6 w-[480px]" onClick={(e) => e.stopPropagation()}>
        <h2 className="text-h3 font-semibold mb-6">회생 승인</h2>
        
        <div className="mb-6">
          <p className="text-p1 mb-2">이름</p>
          <Card className="bg-background">
            <p className="text-p1">{studentName}</p>
          </Card>
        </div>

        <div className="flex gap-6 mb-6">
          <div className="flex-1">
            <p className="text-p1 mb-2">신용등급</p>
            <Card className="bg-background">
              <p className="text-p1">{creditGrade}</p>
            </Card>
          </div>

          <div className="flex-1">
            <p className="text-p1 mb-2">자산</p>
            <Card className="bg-background">
              <Lufin size="m" count={currentAsset} />
            </Card>
          </div>
        </div>

        <div className="flex gap-6 mb-6">
          <div className="flex-1">
            <p className="text-p1 mb-2">대출</p>
            <Card className="bg-background">
              <Lufin size="m" count={loanAmount} />
            </Card>
          </div>

          <div className="flex-1">
            <p className="text-p1 mb-2">투자</p>
            <Card className="bg-background">
              <Lufin size="m" count={investmentAmount} />
            </Card>
          </div>
        </div>

        <div className="flex flex-col gap-4 mb-6">
          <div className="flex items-start gap-2">
            <input 
              type="checkbox" 
              className="mt-1" 
              id="check1" 
              checked={checkboxes.check1}
              onChange={() => handleSingleCheck('check1')}
            />
            <label htmlFor="check1" className="text-p2 text-grey">
              학생과 상담을 진행했나요?
            </label>
          </div>
          <div className="flex items-start gap-2">
            <input 
              type="checkbox" 
              className="mt-1" 
              id="check2" 
              checked={checkboxes.check2}
              onChange={() => handleSingleCheck('check2')}
            />
            <label htmlFor="check2" className="text-p2 text-grey">
              학생이 회생 계획서를 작성했나요?
            </label>
          </div>
          <div className="flex items-start gap-2">
            <input 
              type="checkbox" 
              className="mt-1" 
              id="check3" 
              checked={checkboxes.check3}
              onChange={() => handleSingleCheck('check3')}
            />
            <label htmlFor="check3" className="text-p2 text-grey">
              학생이 회생 계획서를 이해하고 있나요?
            </label>
          </div>
          <div className="flex items-start gap-2">
            <input 
              type="checkbox" 
              className="mt-1" 
              id="check4" 
              checked={checkboxes.check4}
              onChange={() => handleSingleCheck('check4')}
            />
            <label htmlFor="check4" className="text-p2 text-grey">
              학생이 회생 계획서를 실천할 의지가 있나요?
            </label>
          </div>
          <div className="flex items-start gap-2">
            <input 
              type="checkbox" 
              className="mt-1" 
              id="check5" 
              checked={checkboxes.check5}
              onChange={() => handleSingleCheck('check5')}
            />
            <label htmlFor="check5" className="text-p2 text-grey">
              학생과 F+로 바뀌기 위한 일정을 잡았나요?
            </label>
          </div>
        </div>

        <div className="flex flex-col gap-3">
          <Button
            color="neutral"
            size="md"
            full
            onClick={handleCheckAll}
          >
            {isAllChecked ? '모두 해제' : '모두 동의'}
          </Button>
          <div className="flex gap-3">
            <Button
              color="neutral"
              size="md"
              full
              onClick={onClose}
            >
              취소하기
            </Button>
            <Button
              color={isAllChecked ? "primary" : "disabled"}
              size="md"
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