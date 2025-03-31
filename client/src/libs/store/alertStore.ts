import { ReactNode } from 'react';
import { create } from 'zustand';

interface AlertButton {
  label: string;
  onClick: () => void;
  variant?: 'solid' | 'outline' | 'ghost';
  color?: 'primary' | 'danger' | 'neutral' | 'info';
}

interface AlertState {
  isVisible: boolean;
  isOpening: boolean;
  title: string;
  item: ReactNode | null; // 새로운 item 필드 추가
  description: string;
  status: 'info' | 'warning' | 'danger' | 'success';
  primaryButton: AlertButton | null;
  secondaryButton: AlertButton | null;
  showAlert: (
    title: string,
    item: ReactNode | null, // 새로운 item 필드 추가
    description: string,
    status: 'info' | 'warning' | 'danger' | 'success',
    primaryButton: AlertButton,
    secondaryButton?: AlertButton | null,
  ) => void;
  hideAlert: () => void;
}

const useAlertStore = create<AlertState>((set) => ({
  isVisible: false,
  isOpening: false,
  title: '',
  item: null, // item 초기값 null로 설정
  description: '',
  status: 'info',
  primaryButton: null,
  secondaryButton: null,
  showAlert: (title, item, description, status, primaryButton, secondaryButton = null) =>
    set({
      isVisible: true,
      title,
      item,
      description,
      status,
      primaryButton,
      secondaryButton,
    }),
  hideAlert: () => {
    set({ isOpening: false });
    const animationDuration = 200;

    setTimeout(() => set({ isVisible: false }), animationDuration);
    setTimeout(
      () =>
        set({
          title: '',
          item: null,
          description: '',
          status: 'info',
          primaryButton: null,
          secondaryButton: null,
        }),
      animationDuration,
    );
  },
}));

export default useAlertStore;
