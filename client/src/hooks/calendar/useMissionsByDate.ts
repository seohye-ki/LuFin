import { useCallback } from 'react';
import moment from 'moment';
import { missions } from '../../types/mission/mission';

export const useMissionsByDate = () => {
  const getFormattedKey = useCallback((date: Date) => {
    return moment(date).format('YYYY-MM-DD');
  }, []);

  const getMissionsByDate = useCallback((dateKey: string) => {
    return missions.filter((mission) => mission.missionDate === dateKey);
  }, []);

  return {
    getFormattedKey,
    getMissionsByDate,
  };
};
