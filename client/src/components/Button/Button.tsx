import { ButtonHTMLAttributes } from 'react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  color?: 'primary' | 'danger' | 'neutral' | 'disabled' | 'info';
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  variant?: 'solid' | 'outline' | 'ghost';
  children: React.ReactNode;
  onClick?: () => void;
  disabled?: boolean;
  className?: string;
  full?: boolean;
}

export default function Button({
  color = 'primary',
  size = 'md',
  variant = 'solid',
  children,
  className = '',
  full = false,
  ...props
}: ButtonProps) {
  const baseStyles =
    'rounded-[24px] font-semibold transition-colors focus-visible:outline-2 focus-visible:outline-offset-2';

  const variantStyles = {
    solid: {
      primary: 'bg-light-cyan text-black hover:bg-light-cyan-30 focus-visible:outline-light-cyan',
      danger: 'bg-danger text-white hover:opacity-80 focus-visible:outline-danger',
      neutral: 'bg-new-grey text-black hover:bg-grey-30 focus-visible:outline-new-grey',
      disabled: 'bg-light-grey text-white cursor-not-allowed',
      info: 'bg-info text-white hover:opacity-80 focus-visible:outline-info',
    },
    outline: {
      primary: 'border-2 border-light-cyan text-light-cyan bg-transparent hover:bg-light-cyan-30',
      danger: 'border-2 border-danger text-danger bg-transparent hover:bg-danger/10',
      neutral: 'border-2 border-new-grey text-new-grey bg-transparent hover:bg-grey-30',
      disabled: 'border-2 border-light-grey text-light-grey bg-transparent cursor-not-allowed',
      info: 'border-2 border-info text-info bg-transparent hover:bg-info/10',
    },
    ghost: {
      primary: 'text-light-cyan hover:bg-light-cyan-30',
      danger: 'text-danger hover:bg-danger/10',
      neutral: 'text-new-grey hover:bg-grey-30',
      disabled: 'text-light-grey cursor-not-allowed',
      info: 'text-info hover:bg-info/10',
    },
  };

  const sizeStyles = {
    xs: 'px-4 py-1.5 text-c2',
    sm: 'px-4.5 py-1.5 text-c1',
    md: 'px-5 py-2 text-p1',
    lg: 'px-5.5 py-2.5 text-h3',
    xl: 'px-6 py-2.5 text-h2',
  };

  return (
    <button
      type='button'
      className={`
        ${baseStyles}
        ${variantStyles[variant][color]}
        ${sizeStyles[size]}
        ${full ? 'w-full' : ''}
        ${className}
      `}
      disabled={color === 'disabled'}
      {...props}
    >
      {children}
    </button>
  );
}
