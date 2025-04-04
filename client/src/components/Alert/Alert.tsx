import { useEffect } from 'react';
import { Icon } from '../Icon/Icon';
import Button from '../Button/Button';
import useAlertStore from '../../libs/store/alertStore';

const statusConfig = {
  info: { icon: 'InfoCircle', color: 'info' },
  warning: { icon: 'Notification', color: 'warning' },
  danger: { icon: 'CloseCircle', color: 'danger' },
  success: { icon: 'TickCircle', color: 'success' },
} as const;

const Alert = () => {
  const { isVisible, isOpening, title, item, description, status, primaryButton, secondaryButton } =
    useAlertStore((state) => state);

  useEffect(() => {
    if (isVisible) {
      setTimeout(() => {
        useAlertStore.setState({ isOpening: true });
      }, 0);
    }
  }, [isVisible]);

  console.log(status);
  const { icon, color } = statusConfig[status];

  return (
    isVisible && (
      <div className='fixed top-0 left-0 right-0 bottom-0 flex justify-center items-center bg-black/80 z-50'>
        <div
          className={`w-96 h-fit p-8 rounded-2xl gap-8 bg-white flex flex-col items-center transition-all ease-out duration-300 ${isOpening ? 'scale-100 opacity-100' : 'scale-100 opacity-0'}`}
        >
          <div className='w-full h-full flex flex-col items-center gap-2'>
            <Icon name={icon} size={42} color={color} variant='Bold' />
            <h3 className='text-h3 font-bold text-black'>{title}</h3>
            <div className='w-full'>{item}</div>
            {description && <p className='text-p2 text-dark-grey text-center'>{description}</p>}
          </div>
          <div className='w-full flex flex-col sm:flex-row gap-2'>
            <Button
              variant={primaryButton?.variant || 'solid'}
              color={primaryButton?.color || 'primary'}
              size='md'
              onClick={primaryButton?.onClick}
              className='w-full text-sm whitespace-normal'
            >
              {primaryButton?.label}
            </Button>
            {secondaryButton && (
              <Button
                variant={secondaryButton?.variant || 'solid'}
                color={secondaryButton?.color || 'neutral'}
                size='md'
                onClick={secondaryButton?.onClick}
                className='w-full text-sm whitespace-normal'
              >
                {secondaryButton?.label}
              </Button>
            )}
          </div>
        </div>
      </div>
    )
  );
};

export default Alert;
