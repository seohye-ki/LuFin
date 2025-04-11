interface ProgressBarProps {
  currentStep: number;
  stepTitles: string[];
}

export default function ProgressBar({ currentStep, stepTitles }: ProgressBarProps) {
  const progressPercentages = [10, 25, 50, 75, 100];
  const progress = progressPercentages[currentStep - 1] || 0;

  return (
    <div className='mb-10'>
      <div className='flex justify-between mb-3'>
        {stepTitles.map((title, index) => (
          <div
            key={title}
            className={`text-p2 font-medium ${
              index + 1 === currentStep ? 'text-info' : 'text-grey'
            }`}
          >
            {title}
          </div>
        ))}
      </div>
      <div className='h-2.5 bg-grey-30 rounded-full'>
        <div
          className='h-full bg-info rounded-full transition-all duration-300'
          style={{ width: `${progress}%` }}
        />
      </div>
    </div>
  );
}
