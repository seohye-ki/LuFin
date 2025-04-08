import React from 'react';
import { forwardRef } from 'react';

type TextFieldVariant = 'normal' | 'success' | 'error';
type TextFieldSize = 'md' | 'lg' | 'xl';

interface TextFieldProps extends Omit<React.InputHTMLAttributes<HTMLInputElement>, 'size'> {
  label?: string;
  description?: string;
  variant?: TextFieldVariant;
  isDisabled?: boolean;
  inputSize?: TextFieldSize;
}

const TextField = forwardRef<HTMLInputElement, TextFieldProps>(
  (
    {
      label,
      description,
      variant = 'normal',
      isDisabled,
      inputSize = 'md',
      className = '',
      ...props
    },
    ref,
  ) => {
    const getVariantStyles = (variant: TextFieldVariant) => {
      switch (variant) {
        case 'success':
          return 'border-info focus:border-info outline-info';
        case 'error':
          return 'border-danger focus:border-danger outline-danger';
        default:
          return 'border-grey focus:border-info outline-broken-white focus:outline-info';
      }
    };

    const getDescriptionColor = (variant: TextFieldVariant) => {
      switch (variant) {
        case 'success':
          return 'text-info';
        case 'error':
          return 'text-danger';
        default:
          return 'text-dark-grey';
      }
    };

    const getSizeStyles = (size: TextFieldSize) => {
      switch (size) {
        case 'xl':
          return 'text-[1.5rem] py-3 px-4';
        case 'lg':
          return 'text-[1.25rem] py-2.5 px-3.5';
        default:
          return 'text-p2 py-1.5 px-3';
      }
    };

    const baseInputClasses =
      'block w-full rounded-md bg-broken-white outline-1 -outline-offset-1 placeholder:text-grey-25 focus:outline-2 focus:-outline-offset-2';
    const sizeClasses = getSizeStyles(inputSize);

    const inputClasses = `${baseInputClasses} ${sizeClasses} ${getVariantStyles(variant)}`;

    const disabledClasses = isDisabled
      ? 'disabled:cursor-not-allowed disabled:bg-broken-white disabled:text-grey disabled:outline-grey-30'
      : '';

    return (
      <div className='flex flex-col'>
        {label && (
          <label
            htmlFor={props.id}
            className={`font-medium text-dark-grey ${
              inputSize === 'xl'
                ? 'text-[1.25rem]'
                : inputSize === 'lg'
                  ? 'text-[1.125rem]'
                  : 'text-c1'
            }`}
          >
            {label}
          </label>
        )}
        <div className='mt-2 relative'>
          <input
            ref={ref}
            disabled={isDisabled}
            className={`${inputClasses} ${disabledClasses} ${className}`}
            aria-invalid={variant === 'error' ? 'true' : undefined}
            aria-describedby={
              variant === 'error'
                ? `${props.id}-error`
                : description
                  ? `${props.id}-description`
                  : undefined
            }
            {...props}
          />
        </div>
        {variant === 'error' && (
          <p
            id={`${props.id}-error`}
            className={`mt-2 text-danger ${
              inputSize === 'xl'
                ? 'text-[1.125rem]'
                : inputSize === 'lg'
                  ? 'text-[1rem]'
                  : 'text-c1'
            }`}
          >
            {description}
          </p>
        )}
        {description && variant !== 'error' && (
          <span
            className={`mt-2 ${getDescriptionColor(variant)} ${
              inputSize === 'xl'
                ? 'text-[1.125rem]'
                : inputSize === 'lg'
                  ? 'text-[1rem]'
                  : 'text-c1'
            }`}
          >
            {description}
          </span>
        )}
      </div>
    );
  },
);

TextField.displayName = 'TextField';

export default TextField;
