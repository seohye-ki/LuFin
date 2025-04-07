import { MissionList, MissionParticipation } from '../../../../types/mission/mission';
import { createMissionRow } from '../../../../libs/utils/mission-util';

export const useStudentMissions = (
  missionList: MissionList[],
  participationList: MissionParticipation[],
  myMemberId: number,
  onRowClick: (mission: MissionList) => void,
) => {
  // 1. 내가 참여한 미션만 필터링
  const myParticipations = participationList.filter((p) => p.memberId === myMemberId);

  // 2. 참여한 미션 → myMissionRows
  const myMissionRows = missionList
    .filter((m) => myParticipations.some((p) => p.missionId === m.missionId))
    .map((mission) => {
      const participation = myParticipations.find((p) => p.missionId === mission.missionId);
      return createMissionRow(mission, participation, onRowClick);
    })
    .filter((row): row is NonNullable<typeof row> => row !== null);

  // 3. 아직 참여 안한 미션 → availableMissionRows
  const availableMissionRows = missionList
    .filter((m) => !myParticipations.some((p) => p.missionId === m.missionId))
    .map((mission) => createMissionRow(mission, undefined, onRowClick))
    .filter((row): row is NonNullable<typeof row> => row !== null);

  return {
    myMissionRows,
    availableMissionRows,
  };
};
