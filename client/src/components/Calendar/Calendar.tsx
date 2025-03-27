import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { useState } from 'react';
import moment from 'moment';
import styles from './Calendar.module.css';
import { Icon } from '../Icon/Icon';

type ValuePiece = Date | null;
type Value = ValuePiece | [ValuePiece, ValuePiece];

function CalendarView() {
  const [value, onChange] = useState<Value>(new Date());
  const [expandedDate, setExpandedDate] = useState<string | null>(null);

  const toDoList: Record<string, string[]> = {
    '2025-03-20': ['칠판 필기 정리하기', '칠판 필기 정리하기', '칠판 필기 정리하기'],
    '2025-03-21': ['회의 준비하기'],
    '2025-03-23': ['문서 제출하기'],
  };

  // 날짜 key 포맷: YYYY-MM-DD
  const getFormattedKey = (date: Date) => moment(date).format('YYYY-MM-DD');

  const handleTodoClick = (dateKey: string) => {
    setExpandedDate(expandedDate === dateKey ? null : dateKey);
  };

  const handlePrevMonth = () => {
    const currentDate = value as Date;
    const prevMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1);
    onChange(prevMonth);
  };

  const handleNextMonth = () => {
    const currentDate = value as Date;
    const nextMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1);
    onChange(nextMonth);
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
      <button className='flex items-center justify-center'>
        <Icon name='CircleEdit' size={34} />
      </button>
    </div>
  );

  return (
    <div className={styles.calendarWrapper}>
      <Navigation
        date={value as Date}
        onClickPrev={handlePrevMonth}
        onClickNext={handleNextMonth}
      />
      <Calendar
        onChange={onChange}
        value={value}
        locale='ko-KR'
        showNavigation={false}
        formatDay={(locale, date) => moment(date).format('D')}
        formatShortWeekday={(locale, date) =>
          ['일', '월', '화', '수', '목', '금', '토'][date.getDay()]
        }
        tileContent={({ date }) => {
          const dateKey = getFormattedKey(date);
          const toDo = toDoList[dateKey];
          if (toDo) {
            const isExpanded = expandedDate === dateKey;
            const displayItems = isExpanded ? toDo : toDo.slice(0, 2);
            const hasMore = toDo.length > 2;

            return (
              <div className={styles.todoList}>
                {displayItems.map((item, index) => (
                  <div key={index} className={styles.todoItem}>
                    {item}
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
                    {isExpanded ? '접기' : `${toDo.length - 2} more`}
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
