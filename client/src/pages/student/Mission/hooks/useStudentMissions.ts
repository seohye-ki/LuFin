import { MissionList, MissionParticipation } from '../../../../types/mission/mission';
import { createMissionRow } from '../../../../libs/utils/mission-util';

export const useStudentMissions = (
  myMissions: { mission: MissionList; participation: MissionParticipation }[],
  availableMissions: MissionList[],
  onRowClick: (mission: MissionList, participationId?: number) => void,
) => {
  const myMissionRows = myMissions
    .map(({ mission, participation }) => createMissionRow(mission, participation, onRowClick))
    .filter((row): row is NonNullable<typeof row> => row !== null);

  const availableMissionRows = availableMissions
    .map((mission) => createMissionRow(mission, undefined, onRowClick))
    .filter((row): row is NonNullable<typeof row> => row !== null);

  return {
    myMissionRows,
    availableMissionRows,
  };
};
