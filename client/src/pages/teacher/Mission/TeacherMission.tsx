import CalendarView from '../../../components/Calendar/Calendar';
import DefaultLayout from '../../../components/Layout/DefaultLayout';
import SidebarLayout from '../../../components/Layout/SidebarLayout';

const TeacherMission = () => {
  return (
    <div>
      <SidebarLayout>
        <div>
          <CalendarView />
        </div>
      </SidebarLayout>
    </div>
  );
};

export default TeacherMission;
