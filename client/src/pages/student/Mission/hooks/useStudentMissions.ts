import { MissionDetail, MissionParticipation } from '../../../../types/mission/mission';
import { createMissionRow } from '../../../../libs/utils/mission-util';

export const useStudentMissions = (
  myMemberId: number,
  missionDetails: MissionDetail[],
  missionParticipations: MissionParticipation[],
  onRowClick: (mission: MissionDetail) => void,
) => {
  // 내가 참여한 미션 데이터
  const myParticipations = missionParticipations.filter(
    (participation) => participation.memberId === myMemberId,
  );

  const myMissionRows = myParticipations
    .map((participation) => {
      const mission = missionDetails.find((m) => m.missionId === participation.missionId);
      if (!mission) return null;
      return createMissionRow(mission, participation, onRowClick);
    })
    .filter((row): row is NonNullable<typeof row> => row !== null);

  // 수행 가능한 미션 데이터
  const availableMissions = missionDetails.filter(
    (mission) =>
      mission.status === 'RECRUITING' &&
      mission.currentParticipant < mission.maxParticipant &&
      !myParticipations.some((p) => p.missionId === mission.missionId),
  );

  const availableMissionRows = availableMissions.map((mission) =>
    createMissionRow(mission, undefined, onRowClick),
  );

  return {
    myMissionRows,
    availableMissionRows,
  };
};
