import React, { useState, useEffect } from 'react';
import Button from '../../../../components/Button/Button';
import Card from '../../../../components/Card/Card';
import TextField from '../../../../components/Form/TextField';
import { addItem } from '../../../../libs/services/shop/shop.service';

const CreateModal: React.FC<{ closeModal: () => void; onSuccess: () => void }> = ({
  closeModal,
  onSuccess,
}) => {
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [amount, setAmount] = useState('');
  const [expirationDate, setExpirationDate] = useState('');

  const [isFormValid, setIsFormValid] = useState(false);

  // 유효성 검사 함수들
  const isValidName = name.length >= 2 && name.length <= 30;
  const parsedPrice = Number(price);
  const isValidPrice = !isNaN(parsedPrice) && parsedPrice >= 100 && parsedPrice <= 10000000;
  const parsedAmount = Number(amount);
  const isValidAmount = !isNaN(parsedAmount) && parsedAmount >= 1 && parsedAmount <= 1000;
  const isValidDate = (() => {
    const now = new Date();
    const input = new Date(expirationDate);
    return input > now;
  })();

  useEffect(() => {
    setIsFormValid(isValidName && isValidPrice && isValidAmount && isValidDate);
  }, [name, price, amount, expirationDate]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!isFormValid) return;

    const isoDate = new Date(expirationDate).toISOString().split('.')[0];

    const res = await addItem(name, parsedPrice, parsedAmount, isoDate);

    if (res) {
      onSuccess();
    }
    closeModal();
  };

  return (
    <div
      className='fixed top-0 left-0 right-0 bottom-0 flex justify-center items-center bg-black/80 z-50'
      onClick={closeModal}
    >
      <div onClick={(e) => e.stopPropagation()}>
        <Card className='w-104 h-fit' titleLeft='새로운 아이템 생성'>
          <form onSubmit={handleSubmit} className='flex flex-col gap-4'>
            <TextField
              label='이름'
              placeholder='이름을 입력해주세요.'
              value={name}
              onChange={(e) => setName(e.target.value)}
              variant={!isValidName && name ? 'error' : 'normal'}
              description={!isValidName && name ? '이름은 2자 이상, 30자 이하로 입력해주세요.' : ''}
            />
            <TextField
              label='가격'
              placeholder='가격을 입력해주세요.'
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              variant={!isValidPrice && price ? 'error' : 'normal'}
              description={
                !isValidPrice && price ? '가격은 100 이상 1,000만 이하의 숫자여야 합니다.' : ''
              }
            />
            <TextField
              label='수량'
              placeholder='수량을 입력해주세요.'
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              variant={!isValidAmount && amount ? 'error' : 'normal'}
              description={
                !isValidAmount && amount ? '수량은 1 이상 1,000 이하의 숫자여야 합니다.' : ''
              }
            />
            <TextField
              label='기한'
              type='date'
              value={expirationDate}
              onChange={(e) => setExpirationDate(e.target.value)}
              variant={!isValidDate && expirationDate ? 'error' : 'normal'}
              description={
                !isValidDate && expirationDate ? '기한은 현재 시각보다 이후여야 합니다.' : ''
              }
            />

            <div className='w-full flex flex-row items-center gap-4 mt-4'>
              <Button color='neutral' type='button' className='w-full' onClick={closeModal}>
                취소
              </Button>
              <Button
                type='submit'
                className='w-full'
                disabled={!isFormValid}
                color={isFormValid ? 'primary' : 'disabled'}
              >
                {isFormValid ? '생성' : '입력을 완료해주세요.'}
              </Button>
            </div>
          </form>
        </Card>
      </div>
    </div>
  );
};

export default CreateModal;
