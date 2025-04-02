import MissionForm from './MissionForm';

interface MissionCreateModalProps {
  onClose: () => void;
}

const MissionCreateModal = ({ onClose }: MissionCreateModalProps) => {
  return (
    <div className='fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[416px] max-h-screen rounded-xl border-t border-new-grey shadow-lg z-20'>
      <MissionForm onClose={onClose} mode='create' />
    </div>
  );
};

export default MissionCreateModal;
