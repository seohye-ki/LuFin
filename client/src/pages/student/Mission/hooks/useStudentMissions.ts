import {
  MissionList,
  MissionParticipation,
  ParticipationUserInfo,
} from '../../../../types/mission/mission';
import { createMissionRow } from '../../../../libs/utils/mission-util';
import useMissionStore from '../../../../libs/store/missionStore';

const convertToMissionParticipations = (
  users: ParticipationUserInfo[],
  memberId: number,
  missionId: number,
): MissionParticipation[] => {
  return users.map((user) => ({
    participationId: user.participationId,
    memberId,
    missionId,
    status: user.status,
  }));
};

export const useStudentMissions = (
  missionList: MissionList[],
  participationList: ParticipationUserInfo[],
  myMemberId: number,
  onRowClick: (mission: MissionList) => void,
  setParticipationList: (list: ParticipationUserInfo[]) => void,
) => {
  const getParticipationList = useMissionStore((state) => state.getParticipationList);

  const refetchParticipations = async (missionId: number) => {
    const result = await getParticipationList(missionId);
    if (result.success && result.participations) {
      setParticipationList(result.participations);
    }
  };

  const allMissionParticipations: MissionParticipation[] = missionList.flatMap((mission) =>
    convertToMissionParticipations(participationList, myMemberId, mission.missionId),
  );

  const myParticipations = allMissionParticipations.filter((p) => p.memberId === myMemberId);

  const myMissionRows = missionList
    .filter((m) => myParticipations.some((p) => p.missionId === m.missionId))
    .map((mission) => {
      const participation = myParticipations.find((p) => p.missionId === mission.missionId);
      return createMissionRow(mission, participation, onRowClick);
    })
    .filter((row): row is NonNullable<typeof row> => row !== null);

  const availableMissionRows = missionList
    .filter((m) => !myParticipations.some((p) => p.missionId === m.missionId))
    .map((mission) => createMissionRow(mission, undefined, onRowClick))
    .filter((row): row is NonNullable<typeof row> => row !== null);

  return {
    myMissionRows,
    availableMissionRows,
    refetchParticipations,
  };
};
