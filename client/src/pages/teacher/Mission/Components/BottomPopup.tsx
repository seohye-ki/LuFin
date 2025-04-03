import { FC } from 'react';
import styles from './BottomPopup.module.css';

type BottomPopupProps = {
  isOpen: boolean;
  onClose: () => void;
  children?: React.ReactNode;
};

const BottomPopup: FC<BottomPopupProps> = ({ isOpen, children }) => {
  if (!isOpen) return null;

  return <div className={styles.content}>{children}</div>;
};

export default BottomPopup;
