interface DropdownMenuItemProps {
  label: string;
  onClick: (e: React.MouseEvent) => void;
}

export const DropdownMenuItem = ({ label, onClick }: DropdownMenuItemProps) => {
  return (
    <button
      className='w-full px-3 py-2 text-left text-p2 text-grey hover:bg-light-cyan hover:text-info transition-colors'
      onClick={onClick}
    >
      {label}
    </button>
  );
};
