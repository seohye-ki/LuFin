import moment from 'moment';
import useMissionStore from '../../libs/store/missionStore';

export const useCalendarNavigation = (currentDate: Date, onDateChange: (date: Date) => void) => {
  const { getMissionList } = useMissionStore();

  const goPrevMonth = () => {
    onDateChange(moment(currentDate).subtract(1, 'month').toDate());
    getMissionList();
  };

  const goNextMonth = () => {
    onDateChange(moment(currentDate).add(1, 'month').toDate());
    getMissionList();
  };

  return {
    goPrevMonth,
    goNextMonth,
  };
};
