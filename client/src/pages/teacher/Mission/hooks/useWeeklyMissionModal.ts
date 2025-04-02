import { useState } from 'react';
import { MissionDetail } from '../../../../types/Mission/mission';

export const useWeeklyMissionModal = () => {
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [selectedMission, setSelectedMission] = useState<MissionDetail | null>(null);

  const onSelectMission = (mission: MissionDetail) => {
    setSelectedMission(mission);
  };

  const onCloseModal = () => {
    setSelectedMission(null);
    setShowCreateModal(false);
  };

  return {
    showCreateModal,
    selectedMission,
    onSelectMission,
    onCloseModal,
    setShowCreateModal,
  };
};
