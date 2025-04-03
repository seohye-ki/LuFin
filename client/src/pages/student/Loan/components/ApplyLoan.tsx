import React, { useState } from 'react';
import Card from '../../../../components/Card/Card';
import TextField from '../../../../components/Form/TextField';
import { LoanProductDTO } from '../../../../types/Loan/loan';
import Button from '../../../../components/Button/Button';
import Checkbox from '../../../../components/Form/Checkbox';

interface ApplyLoanProps {
  loanProduct: LoanProductDTO;
  closeModal: () => void;
}

const agreementTexts = [
  '심사 결과에 따라 승인되지 않을 수 있습니다.',
  '대출 실행 후 취소가 불가능하며, 대출금 상환 의무가 발생합니다.',
  '대출은 동시에 한 개만 진행 가능하며, 중복으로 신청할 수 없습니다.',
  '연체 시 신용등급 하락 등의 불이익이 발생할 수 있습니다.',
  '대출 신청 전 상품설명서 및 약관을 충분히 확인하였습니다.',
];

const ApplyLoan: React.FC<ApplyLoanProps> = ({ loanProduct, closeModal }) => {
  const [requiredAmount, setRequiredAmount] = useState<number | null>(null);
  const [description, setDescription] = useState<string>('');
  const [expectedInterestAmount, setExpectedInterestAmount] = useState<number>(0);
  const [agreements, setAgreements] = useState<boolean[]>(
    new Array(agreementTexts.length).fill(false),
  );

  const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/,/g, '');
    if (!value) {
      setRequiredAmount(null);
      setExpectedInterestAmount(0);
      return;
    }

    const amount = parseFloat(value);
    if (isNaN(amount)) return;

    const interestRate = loanProduct.interestRate ?? 0;
    const interestAmount = (amount * interestRate) / 100;

    setRequiredAmount(amount);
    setExpectedInterestAmount(interestAmount);
  };

  const handleAgreementChange = (index: number) => {
    const updatedAgreements = [...agreements];
    updatedAgreements[index] = !updatedAgreements[index];
    setAgreements(updatedAgreements);
  };

  const isAllAgreed = agreements.every((agreed) => agreed);

  return (
    <div
      className='fixed top-0 left-0 right-0 bottom-0 flex justify-center items-center bg-black/80 z-50'
      onClick={closeModal}
    >
      <div onClick={(e) => e.stopPropagation()}>
        <Card className='w-110 h-fit' titleLeft='대출 신청'>
          <div className='flex flex-col gap-4'>
            <TextField label='신청자' placeholder='이름을 입력해주세요.' value='신청자' disabled />
            <div className='flex flex-row gap-4'>
              <TextField label='상품명' placeholder='상품명' value={loanProduct.name} disabled />
              <TextField
                label='만기일'
                placeholder='만기일'
                value={
                  loanProduct.period
                    ? new Date(
                        Date.now() + loanProduct.period * 24 * 60 * 60 * 1000,
                      ).toLocaleDateString()
                    : '만기일 없음'
                }
                disabled
              />
            </div>
            <div className='flex flex-row gap-4'>
              <TextField
                label='금액'
                placeholder='대출 금액을 입력해주세요.'
                value={requiredAmount !== null ? requiredAmount.toLocaleString() : ''}
                onChange={handleAmountChange}
              />
              <TextField
                label='예상 이자액'
                placeholder='예상 이자액'
                value={expectedInterestAmount.toLocaleString()}
                disabled
              />
            </div>
            <TextField
              label='대출 사유'
              placeholder='대출 사유를 입력해주세요.'
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />

            {/* ✅ 약관 동의 체크박스 여러 개 추가 */}
            <div className='flex flex-col'>
              {agreementTexts.map((text, index) => (
                <div key={index} className='flex items-center'>
                  <Checkbox
                    id={`agreement-${index}`}
                    checked={agreements[index]}
                    onChange={() => handleAgreementChange(index)}
                  />
                  <label
                    htmlFor={`agreement-${index}`}
                    className='text-c1 text-grey cursor-pointer'
                  >
                    {text}
                  </label>
                </div>
              ))}
            </div>
          </div>

          {/* ✅ 모든 약관에 동의해야 신청 버튼 활성화 */}
          <div className='flex flex-row gap-4'>
            <Button className='w-full' color='neutral' onClick={closeModal}>
              취소
            </Button>
            <Button className='w-full' disabled={!isAllAgreed}>
              신청하기
            </Button>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default ApplyLoan;
