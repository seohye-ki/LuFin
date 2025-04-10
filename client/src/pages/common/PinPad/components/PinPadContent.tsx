import Button from '../../../../components/Button/Button';

interface PinPadContentProps {
  numbers: number[];
  handleNumber: (n: number) => void;
  handleClear: () => void;
  handleDelete: () => void;
}

const PinPadContent = ({
  numbers,
  handleNumber,
  handleClear,
  handleDelete,
}: PinPadContentProps) => {
  const digits = numbers.slice(0, 9);
  const lastDigit = numbers[9];

  return (
    <div className='grid grid-cols-3 gap-2 w-full'>
      {digits.map((num) => (
        <Button
          key={num}
          onClick={() => handleNumber(num)}
          variant='ghost'
          color='info'
          className='aspect-3/2'
        >
          {num}
        </Button>
      ))}
      <Button onClick={handleClear} variant='ghost' color='danger' className='aspect-3/2'>
        전체 삭제
      </Button>
      <Button
        onClick={() => handleNumber(lastDigit)}
        variant='ghost'
        color='info'
        className='aspect-3/2'
      >
        {lastDigit}
      </Button>
      <Button onClick={handleDelete} variant='ghost' color='danger' className='aspect-3/2'>
        삭제
      </Button>
    </div>
  );
};

export default PinPadContent;
