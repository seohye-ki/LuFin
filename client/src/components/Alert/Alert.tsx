import { Icon } from '../Icon/Icon';
import Button from '../Button/Button';

interface AlertButton {
  label: string;
  onClick: () => void;
  variant?: 'solid' | 'outline' | 'ghost';
  color?: 'primary' | 'danger' | 'neutral' | 'info';
}

interface AlertProps {
  title: string;
  description?: React.ReactNode;
  status: 'info' | 'warning' | 'danger' | 'success';
  primaryButton: AlertButton;
  secondaryButton?: AlertButton;
  buttonDirection?: 'row' | 'column';
  className?: string;
}

const statusConfig = {
  info: {
    icon: 'InfoCircle',
    color: 'info',
  },
  warning: {
    icon: 'Notification',
    color: 'warning',
  },
  danger: {
    icon: 'CloseCircle',
    color: 'danger',
  },
  success: {
    icon: 'TickCircle',
    color: 'success',
  },
} as const;

const Alert = ({
  title,
  description,
  status,
  primaryButton,
  secondaryButton,
  buttonDirection = 'column',
  className = '',
}: AlertProps) => {
  const { icon, color } = statusConfig[status];

  return (
    <div className={`flex flex-col items-center p-4 rounded-2xl bg-white w-80 h-fit ${className}`}>
      {/* Icon */}
      <Icon
        name={icon}
        size={42}
        color={color}
        variant="Bold"
        className="mb-4"
      />

      {/* Text Content */}
      <div className="flex flex-col items-center text-center mb-6">
        <h3 className="text-h3 font-bold text-black mb-2">{title}</h3>
        {description && (
          <p className="text-p2 text-dark-grey">{description}</p>
        )}
      </div>

      {/* Buttons */}
      <div 
        className={`w-full flex ${
          buttonDirection === 'row' 
            ? 'flex-row gap-3 [&>*]:flex-1' 
            : 'flex-col gap-2'
        }`}
      >
        <Button
          variant={primaryButton.variant || 'solid'}
          color={primaryButton.color || 'primary'}
          size="md"
          full={buttonDirection === 'column'}
          onClick={primaryButton.onClick}
        >
          {primaryButton.label}
        </Button>
        
        {secondaryButton && (
          <Button
            variant={secondaryButton.variant || 'solid'}
            color={secondaryButton.color || 'neutral'}
            size="md" 
            full={buttonDirection === 'column'}
            onClick={secondaryButton.onClick}
          >
            {secondaryButton.label}
          </Button>
        )}
      </div>
    </div>
  );
};

export default Alert; 