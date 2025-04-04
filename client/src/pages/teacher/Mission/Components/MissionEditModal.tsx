import { MissionDetail } from '../../../../types/mission/mission';
import MissionForm from './MissionForm';

interface MissionEditModalProps {
  mission: MissionDetail;
  onClose: () => void;
}

const MissionEditModal = ({ mission, onClose }: MissionEditModalProps) => {
  return (
    <>
      {/* 오버레이 */}
      <div className='fixed inset-0 bg-black z-10' style={{ opacity: 0.5 }} onClick={onClose} />
      {/* 모달 */}
      <div className='fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[416px] max-h-screen rounded-xl border-t border-new-grey shadow-lg z-20 bg-white'>
        <MissionForm onClose={onClose} defaultValues={mission} mode='edit' />
      </div>
    </>
  );
};

export default MissionEditModal;
