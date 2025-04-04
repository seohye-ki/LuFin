import moment from 'moment';

export const useCalendarNavigation = (currentDate: Date, onDateChange: (date: Date) => void) => {
  const goPrevMonth = () => {
    onDateChange(moment(currentDate).subtract(1, 'month').toDate());
  };

  const goNextMonth = () => {
    onDateChange(moment(currentDate).add(1, 'month').toDate());
  };

  return {
    goPrevMonth,
    goNextMonth,
  };
};
