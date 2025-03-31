import { type ReactNode } from 'react';

interface ContainerProps {
  className?: string;
  children: ReactNode;
}

export function Container({ className, children, ...props }: ContainerProps) {
  return (
    <div
      className={`mx-auto max-w-7xl px-6 lg:px-8 ${className}`}
      {...props}
    >
      {children}
    </div>
  );
} 