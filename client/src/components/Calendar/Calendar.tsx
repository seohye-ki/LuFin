import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { useState } from 'react';
import moment from 'moment';
import styles from './Calendar.module.css';
import { Icon } from '../Icon/Icon';
import { useMissionsByDate } from '../../hooks/calendar/useMissionsByDate';
import { useCalendarNavigation } from '../../hooks/calendar/useCalendarNavigation';
import { MissionList } from '../../types/mission/mission';

type ValuePiece = Date | null;
type Value = ValuePiece | [ValuePiece, ValuePiece];

interface CalendarViewProps {
  onDateSelect?: (date: Date) => void;
}

function CalendarView({ onDateSelect }: CalendarViewProps) {
  const [value, onChange] = useState<Value>(new Date());
  const [expandedDate, setExpandedDate] = useState<string | null>(null);
  const { getDateKeyFromMissionDate, getMissionsByDate } = useMissionsByDate();
  const { goPrevMonth, goNextMonth } = useCalendarNavigation(value as Date, (date: Date) =>
    onChange(date),
  );

  const handleDateChange = (newValue: Value) => {
    onChange(newValue);
    if (onDateSelect && newValue instanceof Date) {
      onDateSelect(newValue);
    }
  };

  const handleTodoClick = (dateKey: string) => {
    setExpandedDate(expandedDate === dateKey ? null : dateKey);
  };

  const Navigation = ({
    date,
    onClickPrev,
    onClickNext,
  }: {
    date: Date;
    onClickPrev: () => void;
    onClickNext: () => void;
  }) => (
    <div className='flex h-16 py-2 justify-between items-center gap-2'>
      <div className='flex items-center gap-2'>
        <button onClick={onClickPrev}>
          <Icon name='ArrowSquareLeft' color='purple' variant='Bold' size={26} />
        </button>
        <span className='text-h2 font-semibold'>{moment(date).format('YYYY년 MM월')}</span>
        <button onClick={onClickNext}>
          <Icon name='ArrowSquareRight' color='purple' variant='Bold' size={26} />
        </button>
      </div>
      {/* <h1 className='text-h2 font-semibold'>날짜를 클릭해서 미션을 생성해보세요!</h1> */}
    </div>
  );

  return (
    <div className={styles.calendarWrapper}>
      <Navigation date={value as Date} onClickPrev={goPrevMonth} onClickNext={goNextMonth} />
      <Calendar
        onChange={handleDateChange}
        value={value}
        locale='ko-KR'
        showNavigation={false}
        formatDay={(_, date) => date.getDate().toString()}
        formatShortWeekday={(_, date) => ['일', '월', '화', '수', '목', '금', '토'][date.getDay()]}
        tileContent={({ date }: { date: Date }) => {
          const dateKey = getDateKeyFromMissionDate(
            date.getFullYear(),
            date.getMonth(),
            date.getDate(),
          );
          const dayMissions = getMissionsByDate(dateKey);
          if (dayMissions.length > 0) {
            const isExpanded = expandedDate === dateKey;
            const displayItems = isExpanded ? dayMissions : dayMissions.slice(0, 2);
            const hasMore = dayMissions.length > 2;

            return (
              <div className={styles.todoList}>
                {displayItems.map((mission: MissionList) => (
                  <div key={mission.missionId} className={styles.todoItem}>
                    {mission.title}
                  </div>
                ))}
                {hasMore && (
                  <div
                    className={styles.moreButton}
                    onClick={(e) => {
                      e.stopPropagation();
                      handleTodoClick(dateKey);
                    }}
                  >
                    {isExpanded ? '접기' : `${dayMissions.length - 2} more`}
                  </div>
                )}
              </div>
            );
          }
          return null;
        }}
      />
    </div>
  );
}

export default CalendarView;
