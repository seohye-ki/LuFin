interface PinDotsProps {
  pin: string;
  maxLength: number;
}

const PinDots = ({ pin, maxLength }: PinDotsProps) => (
  <div className='flex justify-center gap-3 my-6'>
    {Array.from({ length: maxLength }).map((_, i) => (
      <div
        key={i}
        className={`w-4 h-4 rounded-full ${
          pin[i] ? 'bg-info' : 'bg-white border-2 border-grey-30'
        }`}
      />
    ))}
  </div>
);

export default PinDots;
