interface BadgeProps {
  status: 'ready' | 'ing' | 'reject' | 'review' | 'fail' | 'done';
  children: React.ReactNode;
  className?: string;
}

export default function Badge({ status, children, className = '' }: BadgeProps) {
  const baseStyles =
    'rounded-[50px] px-3 py-1 text-p1 font-medium inline-flex items-center justify-center gap-1.5';

  const statusStyles = {
    ready: 'bg-light-cyan-30 text-info before:bg-info', // 모집 중
    ing: 'bg-purple-30 text-dark-purple before:bg-dark-purple', // 수행 중
    reject: 'bg-yellow-30 text-warning before:bg-warning', // 반려
    review: 'bg-grey-30 text-grey before:bg-grey', // 검토 중
    fail: 'bg-pink-30 text-danger before:bg-danger', // 실패
    done: 'bg-success-30 text-success before:bg-success', // 성공
  };

  return (
    <span
      className={`${baseStyles} ${statusStyles[status]} ${className} before:content-[''] before:block before:w-1.5 before:h-1.5 before:rounded-full`}
    >
      {children}
    </span>
  );
}
