import { useState, useRef, useEffect } from 'react';

interface UseDropdownMenuReturn {
  isOpen: boolean;
  menuRef: React.RefObject<HTMLDivElement | null>;
  toggleMenu: (e: React.MouseEvent) => void;
  closeMenu: () => void;
}

export const useDropdownMenu = (): UseDropdownMenuReturn => {
  const [isOpen, setIsOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const toggleMenu = (e: React.MouseEvent) => {
    e.stopPropagation();
    setIsOpen(!isOpen);
  };

  const closeMenu = () => setIsOpen(false);

  return {
    isOpen,
    menuRef,
    toggleMenu,
    closeMenu,
  };
};
