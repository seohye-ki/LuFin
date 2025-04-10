import React, { useState } from 'react';
import Card from '../../../../../components/Card/Card';
import TextField from '../../../../../components/Form/TextField';
import { LoanProductDTO } from '../../../../../types/Loan/loan';
import Button from '../../../../../components/Button/Button';
import Checkbox from '../../../../../components/Form/Checkbox';
import { applyLoan } from '../../../../../libs/services/loan/loan.service';

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

  const [amountError, setAmountError] = useState(false);
  const [descriptionError, setDescriptionError] = useState(false);
  const [agreementError, setAgreementError] = useState(false);

  const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/,/g, '');
    if (!value) {
      setRequiredAmount(null);
      setExpectedInterestAmount(0);
      setAmountError(false);
      return;
    }

    const amount = parseFloat(value);
    if (isNaN(amount)) return;

    const interestRate = loanProduct.interestRate ?? 0;
    const interestAmount = (amount * interestRate) / 100;

    const isOverMax = loanProduct.maxAmount !== undefined && amount > loanProduct.maxAmount;

    setRequiredAmount(amount);
    setExpectedInterestAmount(Math.floor(interestAmount * 100));
    setAmountError(isOverMax);
  };

  const handleAgreementChange = (index: number) => {
    const updated = [...agreements];
    updated[index] = !updated[index];
    setAgreements(updated);
    setAgreementError(false);
  };

  const handleApply = async () => {
    const isAmountValid =
      requiredAmount !== null &&
      requiredAmount > 0 &&
      requiredAmount <= (loanProduct.maxAmount ?? Infinity);
    const isDescriptionValid = description.trim().length >= 5;
    const isAllAgreed = agreements.every(Boolean);

    setAmountError(!isAmountValid);
    setDescriptionError(!isDescriptionValid);
    setAgreementError(!isAllAgreed);

    if (isAmountValid && isDescriptionValid && isAllAgreed) {
      await applyLoan({
        loanProductId: loanProduct.loanProductId,
        requestedAmount: requiredAmount!,
        description,
      });
      closeModal();
    }
  };

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
                description={`최대 ${loanProduct.maxAmount?.toLocaleString()} 루핀`}
                onChange={handleAmountChange}
                variant={amountError ? 'error' : 'normal'}
              />
              <TextField
                label='예상 주 이자액'
                placeholder='예상 이자액'
                value={expectedInterestAmount.toLocaleString()}
                description={`이자율 ${Math.floor(loanProduct.interestRate * 100)}%`}
                disabled
              />
            </div>

            <TextField
              label='대출 사유'
              placeholder='대출 사유를 입력해주세요.'
              value={description}
              onChange={(e) => {
                const value = e.target.value;
                setDescription(value);
                setDescriptionError(value.trim().length < 5);
              }}
              variant={descriptionError ? 'error' : 'normal'}
              description={descriptionError ? '5글자 이상 입력해주세요.' : ''}
            />
          </div>

          <div className='flex flex-col gap-3'>
            {agreementTexts.map((text, index) => (
              <div key={index} className='flex items-center gap-1'>
                <Checkbox
                  id={`agreement-${index}`}
                  checked={agreements[index]}
                  onChange={() => handleAgreementChange(index)}
                />
                <label htmlFor={`agreement-${index}`} className='text-c1 text-grey cursor-pointer'>
                  {text}
                </label>
              </div>
            ))}
            {agreementError && (
              <p className='text-danger text-c1 font-medium'>모든 약관에 동의해주세요.</p>
            )}
          </div>

          <div className='h-fit flex flex-row gap-4'>
            <Button className='w-full' color='neutral' onClick={closeModal}>
              취소
            </Button>
            <Button className='w-full' onClick={handleApply}>
              신청하기
            </Button>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default ApplyLoan;
