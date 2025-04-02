import { MissionDetail } from '../../../../types/mission/mission';
import MissionForm from './MissionForm';

interface MissionEditModalProps {
  mission: MissionDetail;
  onClose: () => void;
}

const MissionEditModal = ({ mission, onClose }: MissionEditModalProps) => {
  return (
    <div className='fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[416px] max-h-screen rounded-xl bg-white shadow-lg z-50'>
      <MissionForm onClose={onClose} defaultValues={mission} mode='edit' />
    </div>
  );
};

export default MissionEditModal;
