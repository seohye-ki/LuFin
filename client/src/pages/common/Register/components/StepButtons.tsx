import Button from '../../../../components/Button/Button';

interface StepButtonsProps {
  onNext: () => void;
  onPrev?: () => void;
  isNextDisabled?: boolean;
  showPrev?: boolean;
}

export default function StepButtons({
  onPrev,
  onNext,
  isNextDisabled = false,
  showPrev = true,
}: StepButtonsProps) {
  return (
    <div className="flex justify-between mt-4">
      {showPrev && onPrev && (
        <Button
          type="button"
          color="neutral"
          size="lg"
          onClick={onPrev}
        >
          이전
        </Button>
      )}
      <Button
        type="button"
        color="primary"
        size="lg"
        disabled={isNextDisabled}
        onClick={onNext}
        className={!showPrev ? 'w-full' : ''}
      >
        다음
      </Button>
    </div>
  );
} 