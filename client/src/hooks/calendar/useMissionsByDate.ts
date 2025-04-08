import { useCallback } from 'react';
import useMissionStore from '../../libs/store/missionStore';

export const useMissionsByDate = () => {
  const { myMissions, availableMissions } = useMissionStore();

  // Date 객체 → 'YYYY-MM-DD' (ISO 형식)
  const getFormattedKey = useCallback((date: Date) => {
    return date.toISOString().split('T')[0];
  }, []);

  const getDateKeyFromMissionDate = useCallback((year: number, month: number, day: number) => {
    const date = new Date(year, month, day);
    return date.toISOString().split('T')[0];
  }, []);

  // 특정 날짜 문자열(YYYY-MM-DD) 기준으로 필터링
  const getMissionsByDate = useCallback(
    (dateKey: string) => {
      return [...myMissions, ...availableMissions].filter((mission) => {
        const missionDate = new Date(mission.missionDate);
        const missionDateKey = missionDate.toISOString().split('T')[0];
        return missionDateKey === dateKey;
      });
    },
    [myMissions, availableMissions],
  );

  return {
    getFormattedKey,
    getDateKeyFromMissionDate,
    getMissionsByDate,
  };
};
