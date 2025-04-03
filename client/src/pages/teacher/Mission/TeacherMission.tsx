import { useState, useRef } from 'react';
import CalendarView from '../../../components/Calendar/Calendar';
import { WeeklyMissionModal } from './Components/WeeklyMissionModal';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import BottomPopup from './Components/BottomPopup';
const TeacherMission = () => {
  const [selectedDate, setSelectedDate] = useState<Date | null>(null);
  const mainRef = useRef<HTMLDivElement>(null);

  const handleDateSelect = (date: Date | null) => {
    setSelectedDate(date);
  };

  return (
    <div className='relative'>
      <SidebarLayout mainRef={mainRef} userRole='teacher'>
        {selectedDate ? (
          <BottomPopup isOpen={true} onClose={() => setSelectedDate(null)}>
            <WeeklyMissionModal selectedDate={selectedDate} onDateChange={handleDateSelect} />
          </BottomPopup>
        ) : (
          <CalendarView onDateSelect={handleDateSelect} />
        )}
      </SidebarLayout>
    </div>
  );
};

export default TeacherMission;
