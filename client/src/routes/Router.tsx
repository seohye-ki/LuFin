import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { paths } from './paths';
import Home from '../pages/common/Home/Home';
import PrivateRoute from './PrivateRoute';
import TeacherMission from '../pages/teacher/Mission/TeacherMission';
import StudentMission from '../pages/student/Mission/StudentMission';
import TeacherStock from '../pages/teacher/Stock/TeacherStock';
import StudentStock from '../pages/student/Stock/StudentStock';
import StudentShop from '../pages/student/Shop/StudentShop';
import Login from '../pages/common/Auth/Login';
import Register from '../pages/common/Auth/Register';
import TeacherClassroom from '../pages/teacher/Classroom/TeacherClassroom';
import StudentLoan from '../pages/student/Loan/StudentLoan';
import StudentDashboard from '../pages/student/Dashboard/StudentDashboard';
import TeacherDashboard from '../pages/teacher/Dashboard/TeacherDashboard';
import TeacherShop from '../pages/teacher/Shop/TeacherShop';
import TeacherLoan from '../pages/teacher/Loan/TeacherLoan';

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path={paths.HOME} element={<Home />} />
        <Route path={paths.LOGIN} element={<Login />} />
        <Route path={paths.REGISTER} element={<Register />} />

        <Route
          path={paths.CLASSROOM}
          element={
            <PrivateRoute
              studentComponent={<TeacherClassroom />}
              teacherComponent={<TeacherClassroom />}
            />
          }
        />
        <Route
          path={paths.DASHBOARD}
          element={
            <PrivateRoute
              studentComponent={<StudentDashboard />}
              teacherComponent={<TeacherDashboard />}
            />
          }
        />
        <Route
          path={paths.SHOP}
          element={
            <PrivateRoute studentComponent={<StudentShop />} teacherComponent={<TeacherShop />} />
          }
        />

        <Route
          path={paths.MISSION}
          element={
            <PrivateRoute
              studentComponent={<StudentMission />}
              teacherComponent={<TeacherMission />}
            />
          }
        />

        <Route
          path={paths.LOAN}
          element={
            <PrivateRoute studentComponent={<StudentLoan />} teacherComponent={<TeacherLoan />} />
          }
        />

        <Route
          path={paths.STOCK}
          element={
            <PrivateRoute studentComponent={<StudentStock />} teacherComponent={<TeacherStock />} />
          }
        />
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
