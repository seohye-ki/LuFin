import { useState, useRef } from 'react';
import CalendarView from '../../../components/Calendar/Calendar';
import { WeeklyMissionModal } from './Components/WeeklyMissionModal';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
// import { Icon } from '../../../components/Icon/Icon';

const TeacherMission = () => {
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const mainRef = useRef<HTMLDivElement>(null);

  const handleDateSelect = (date: Date | null) => {
    setSelectedDate(date);
  };

  return (
    <div className='relative'>
      <SidebarLayout mainRef={mainRef}>
        {selectedDate ? (
          <div>
            <WeeklyMissionModal selectedDate={selectedDate} onDateChange={handleDateSelect} />
          </div>
        ) : (
          <div>
            <CalendarView onDateSelect={handleDateSelect} />
          </div>
        )}
      </SidebarLayout>
    </div>
  );
};

export default TeacherMission;
