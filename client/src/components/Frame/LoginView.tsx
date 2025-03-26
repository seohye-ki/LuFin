import logo from '../../assets/svgs/logo.svg';
import background from '../../assets/svgs/lufin-background.png';
import Button from '../Button/Button';

interface LoginViewProps {
  title: string;
  description?: string;
  content?: React.ReactNode;
  primaryButton?: {
    text: string;
    onClick: () => void;
  };
  secondaryButton?: {
    text: string;
    onClick: () => void;
  };
  backgroundImage?: boolean;
}

const LoginView = ({
  title,
  description,
  content,
  primaryButton,
  secondaryButton,
  backgroundImage,
}: LoginViewProps) => {
  return (
    <div className='flex flex-col w-[352px] h-full items-center justify-center rounded-lg bg-white gap-8 relative'>
      <div className='flex flex-col items-center justify-center p-8 gap-8 relative z-10'>
        <div className='flex flex-col items-center gap-1'>
          <p className='text-c1 font-regular text-info'>교실에서 시작하는 금융교육</p>
          <img src={logo} alt='루핀' className='h-[52px]' />
        </div>

        <div className='flex flex-col items-center gap-2'>
          <h1 className='text-h3 font-semibold text-black'>{title}</h1>
          <p className='text-c1 font-regular text-grey'>{description}</p>
          {content && <div className={`w-full ${backgroundImage ? 'mb-24' : ''}`}>{content}</div>}
        </div>

        <div className='flex px-2 gap-2 justify-center w-full'>
          {primaryButton && (
            <Button size='lg' color='neutral' full onClick={primaryButton.onClick}>
              {primaryButton.text}
            </Button>
          )}
          {secondaryButton && (
            <Button size='lg' color='primary' full onClick={secondaryButton.onClick}>
              {secondaryButton.text}
            </Button>
          )}
        </div>
      </div>
      {backgroundImage && (
        <img
          src={background}
          alt='background'
          className='w-[352px] absolute bottom-0 left-0 object-contain'
        />
      )}
    </div>
  );
};

export default LoginView;
