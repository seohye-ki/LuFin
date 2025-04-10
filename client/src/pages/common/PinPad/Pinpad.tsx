import { useEffect, useRef, useState } from 'react';
import Button from '../../../components/Button/Button';
import { Icon } from '../../../components/Icon/Icon';
import PinDots from './components/PinDots';
import PinPadContent from './components/PinPadContent';

interface PinPadProps {
  onComplete: (pin: string) => void;
  title: string;
  description: string;
  maxLength: number;
}

const PinPad = ({ onComplete, title, description, maxLength }: PinPadProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [pin, setPin] = useState('');
  const [numbers, setNumbers] = useState<number[]>([]);
  const [isOpening, setIsOpening] = useState(false);
  const modalRef = useRef<HTMLDivElement>(null);

  const shuffleNumbers = () => {
    const nums = Array.from({ length: 10 }, (_, i) => i);
    setNumbers(nums.sort(() => Math.random() - 0.5));
  };

  useEffect(() => {
    if (isOpen) {
      setPin('');
      shuffleNumbers();
      setTimeout(() => setIsOpening(true), 0);
    }
  }, [isOpen]);

  const closeModal = () => {
    setIsOpening(false);
    setTimeout(() => setIsOpen(false), 200);
    setPin('');
  };

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (modalRef.current && !modalRef.current.contains(e.target as Node)) {
        closeModal();
      }
    };
    if (isOpen) {
      document.addEventListener('mousedown', handleClickOutside);
    }
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [isOpen]);

  const handleNumber = (n: number) => {
    if (pin.length >= maxLength) return;
    const next = pin + n;
    setPin(next);
    shuffleNumbers();

    if (next.length === maxLength) {
      setTimeout(() => {
        onComplete(next);
        closeModal();
      }, 300);
    }
  };

  const handleDelete = () => {
    setPin((prev) => prev.slice(0, -1));
    shuffleNumbers();
  };

  const handleClear = () => {
    setPin('');
    shuffleNumbers();
  };

  return (
    <>
      <Button
        onClick={() => setIsOpen(true)}
        variant='solid'
        color='primary'
        className='w-full rounded-xl'
      >
        비밀번호 입력
      </Button>

      {isOpen && (
        <div className='fixed inset-0 z-50 flex items-center justify-center bg-black/80'>
          <div
            ref={modalRef}
            className={`w-96 h-fit p-8 rounded-2xl gap-4 bg-white flex flex-col items-center transition-all ease-out duration-300 ${
              isOpening ? 'scale-100 opacity-100' : 'scale-100 opacity-0'
            }`}
          >
            <Icon name='InfoCircle' size={42} color='info' variant='Bold' />
            <h3 className='text-h3 font-bold text-black text-center'>{title}</h3>
            <p className='text-p2 text-dark-grey text-center'>{description}</p>

            <PinDots pin={pin} maxLength={maxLength} />
            <PinPadContent
              numbers={numbers}
              handleNumber={handleNumber}
              handleClear={handleClear}
              handleDelete={handleDelete}
            />

            <div className='w-full flex flex-col sm:flex-row gap-2'>
              <Button onClick={closeModal} color='neutral' size='md' className='w-full text-sm'>
                취소
              </Button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default PinPad;
