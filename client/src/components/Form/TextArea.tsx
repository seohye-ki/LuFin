import { forwardRef } from 'react';
import { Icon } from '../Icon/Icon';

interface TextAreaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  label?: string;
  description?: string;
  error?: string;
  isDisabled?: boolean;
}

const TextArea = forwardRef<HTMLTextAreaElement, TextAreaProps>(
  ({ label, description, error, isDisabled, className = '', ...props }, ref) => {
    const baseInputClasses = "block w-full rounded-md bg-white px-3 py-1.5 text-body1 text-black outline-1 -outline-offset-1 outline-grey placeholder:text-grey-25 placeholder:text-body1 focus:outline-2 focus:-outline-offset-2 focus:outline-light-cyan sm:text-sm/6 resize-none";
    
    const inputClasses = error
      ? `${baseInputClasses.replace('outline-grey', 'outline-danger').replace('focus:outline-light-cyan', 'focus:outline-danger')} pr-10 text-danger placeholder:text-danger`
      : baseInputClasses;

    const disabledClasses = isDisabled
      ? "disabled:cursor-not-allowed disabled:bg-broken-white disabled:text-grey disabled:outline-grey-30"
      : "";

    return (
      <div>
        {label && (
          <label htmlFor={props.id} className="block text-c1 font-medium text-dark-grey">
            {label}
          </label>
        )}
        <div className="mt-2 relative">
          <textarea
            ref={ref}
            disabled={isDisabled}
            className={`${inputClasses} ${disabledClasses} ${className}`}
            aria-invalid={error ? "true" : undefined}
            aria-describedby={error ? `${props.id}-error` : description ? `${props.id}-description` : undefined}
            {...props}
          />
          {error && (
            <div className="absolute right-3 top-3">
              <Icon
                name="InfoCircle"
                size={20}
                color="danger"
                className="pointer-events-none"
              />
            </div>
          )}
        </div>
        {error && (
          <p id={`${props.id}-error`} className="mt-2 text-c1 text-danger">
            {error}
          </p>
        )}
        {description && !error && (
          <p id={`${props.id}-description`} className="mt-2 text-c1 text-grey">
            {description}
          </p>
        )}
      </div>
    );
  }
);

TextArea.displayName = 'TextArea';

export default TextArea;
