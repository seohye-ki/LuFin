import { useState } from 'react';
import { MissionList, MissionRaw } from '../../../../types/mission/mission';
import useMissionStore from '../../../../libs/store/missionStore';
export const useWeeklyMissionModal = () => {
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [selectedMission, setSelectedMission] = useState<MissionRaw | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const getMissionDetail = useMissionStore((state) => state.getMissionDetail);

  const onSelectMission = async (mission: MissionList) => {
    setIsLoading(true);
    const result = await getMissionDetail(mission.missionId);
    if (result.success && result.mission) {
      setSelectedMission(result.mission);
    }
    setIsLoading(false);
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
    isLoading,
  };
};
