import { useState, useEffect, useRef } from 'react';
import CalendarView from '../../../components/Calendar/Calendar';
import { WeeklyMissionModal } from './Components/WeeklyMissionModal';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
// import { Icon } from '../../../components/Icon/Icon';

const TeacherMission = () => {
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const mainRef = useRef<HTMLDivElement>(null);
  const [mainWidth, setMainWidth] = useState<number>(0);

  useEffect(() => {
    if (mainRef.current) {
      setMainWidth(mainRef.current.offsetWidth);
    }

    const handleResize = () => {
      if (mainRef.current) {
        setMainWidth(mainRef.current.offsetWidth);
      }
    };

    window.addEventListener('resize', handleResize);
  }, [mainRef]);

  const handleDateSelect = (date: Date | null) => {
    setSelectedDate(date);
  };

  return (
    <div className='relative'>
      <SidebarLayout mainRef={mainRef}>
        <div>
          <CalendarView onDateSelect={handleDateSelect} />
        </div>
        {selectedDate && (
          <>
            <div
              className='fixed p-4 bottom-0 right-0 rounded-t-2xl bg-white border-t border-new-grey shadow-lg z-20'
              style={{ width: mainWidth }}
            >
              <WeeklyMissionModal selectedDate={selectedDate} onDateChange={handleDateSelect} />
            </div>
          </>
        )}
      </SidebarLayout>
    </div>
  );
};

export default TeacherMission;
