import React from 'react';
import { forwardRef } from 'react';
import { Icon } from '../Icon/Icon';

type TextFieldVariant = 'normal' | 'success' | 'error';

interface TextFieldProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  description?: string;
  variant?: TextFieldVariant;
  isDisabled?: boolean;
}

const TextField = forwardRef<HTMLInputElement, TextFieldProps>(
  ({ label, description, variant = 'normal', isDisabled, className = '', ...props }, ref) => {
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

    const baseInputClasses =
      'block w-full rounded-md bg-broken-white px-3 py-1.5 text-p1 text-black outline-1 -outline-offset-1 placeholder:text-p2 placeholder:text-grey-25 focus:outline-2 focus:-outline-offset-2 sm:text-sm/6';

    const inputClasses = getVariantStyles(variant) + ' ' + baseInputClasses;

    const disabledClasses = isDisabled
      ? 'disabled:cursor-not-allowed disabled:bg-broken-white disabled:text-grey disabled:outline-grey-30'
      : '';

    return (
      <div className='flex flex-col gap-1'>
        {label && (
          <label htmlFor={props.id} className='text-p2 font-medium text-black'>
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
          <p id={`${props.id}-error`} className='mt-2 text-p2 text-danger'>
            {description}
          </p>
        )}
        {description && variant !== 'error' && (
          <span className={`text-sm ${getDescriptionColor(variant)}`}>{description}</span>
        )}
      </div>
    );
  },
);

TextField.displayName = 'TextField';

export default TextField;
