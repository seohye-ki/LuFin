import React from 'react';
import Card from '../../../../components/Card/Card';
import Button from '../../../../components/Button/Button';
import Lufin from '../../../../components/Lufin/Lufin';

interface RecoveryApplicationModalProps {
  studentName: string;
  creditGrade: string;
  currentAsset: number;
  onClose: () => void;
  onSubmit: () => void;
}

const RecoveryApplicationModal: React.FC<RecoveryApplicationModalProps> = ({
  studentName,
  creditGrade,
  currentAsset,
  onClose,
  onSubmit,
}) => {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
      <div className="bg-white rounded-2xl p-6 w-[480px]">
        <h2 className="text-h3 font-semibold mb-6">회생 신청</h2>
        
        <div className="grid grid-cols-3 gap-4 mb-6">
          <Card titleLeft="이름" titleSize="s">
            <p className="text-p1">{studentName}</p>
          </Card>

          <Card titleLeft="신용등급" titleSize="s">
            <p className="text-p1">{creditGrade}</p>
          </Card>

          <Card titleLeft="자산" titleSize="s">
            <Lufin size="m" count={currentAsset} />
          </Card>
        </div>

        <div className="flex flex-col gap-4">
          <div className="flex items-start gap-2">
            <input type="checkbox" className="mt-1" id="check1" />
            <label htmlFor="check1" className="text-p2 text-grey">
              회생 신청 후 선생님과 상담을 진행하겠습니다.
            </label>
          </div>
          <div className="flex items-start gap-2">
            <input type="checkbox" className="mt-1" id="check2" />
            <label htmlFor="check2" className="text-p2 text-grey">
              회생 계획서를 작성하고 실천하겠습니다.
            </label>
          </div>
          <div className="flex items-start gap-2">
            <input type="checkbox" className="mt-1" id="check3" />
            <label htmlFor="check3" className="text-p2 text-grey">
              회생 계획서의 내용을 이해하고 있습니다.
            </label>
          </div>
          <div className="flex items-start gap-2">
            <input type="checkbox" className="mt-1" id="check4" />
            <label htmlFor="check4" className="text-p2 text-grey">
              회생 계획서를 실천할 의지가 있습니다.
            </label>
          </div>
          <div className="flex items-start gap-2">
            <input type="checkbox" className="mt-1" id="check5" />
            <label htmlFor="check5" className="text-p2 text-grey">
              F+로 바뀌기 위한 일정을 선생님과 잡겠습니다.
            </label>
          </div>
        </div>

        <div className="flex gap-3 mt-6">
          <Button
            color="neutral"
            variant="outline"
            size="md"
            full
            onClick={onClose}
          >
            취소하기
          </Button>
          <Button
            color="primary"
            size="md"
            full
            onClick={onSubmit}
          >
            신청하기
          </Button>
        </div>
      </div>
    </div>
  );
};

export default RecoveryApplicationModal; 