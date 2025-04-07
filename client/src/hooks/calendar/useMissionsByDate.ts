import { useCallback } from 'react';
import moment from 'moment';
import useMissionStore from '../../libs/store/missionStore';

export const useMissionsByDate = () => {
  const { missions } = useMissionStore();

  const getFormattedKey = useCallback((date: Date) => {
    return moment(date).format('YYYY-MM-DD');
  }, []);

  const getDateKeyFromMissionDate = (year: number, month: number, day: number) => {
    return `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
  };

  const getMissionsByDate = useCallback(
    (dateKey: string) => {
      return missions.filter((mission) => {
        return moment(mission.missionDate).format('YYYY-MM-DD') === dateKey;
      });
    },
    [missions],
  );

  return {
    getFormattedKey,
    getDateKeyFromMissionDate,
    getMissionsByDate,
  };
};
