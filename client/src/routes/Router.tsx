import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { paths } from './paths';

// pages
import Home from '../pages/common/Home/Home';
import DesignSystem from '../pages/common/DesignSystem/DesignSystem';
import PrivateRoute from './PrivateRoute';
import CommonComponents from '../pages/common/DesignSystem/CommonComponents';
import TeacherMission from '../pages/teacher/Mission/TeacherMission';
import StudentMission from '../pages/student/Mission/StudentMission';
import TeacherStock from '../pages/teacher/Stock/TeacherStock';
import StudentStock from '../pages/student/Stock/StudentStock';
import StudentShop from '../pages/student/Shop/StudentShop';
import Login from '../pages/common/Auth/Login';
import Register from '../pages/common/Auth/Register';
// import TeacherShop from '../pages/teacher/Shop/TeacherShop';
import TeacherClassroom from '../pages/teacher/Classroom/TeacherClassroom';
import StudentClassroom from '../pages/student/Classroom/StudentClassroom';
import StudentLoan from '../pages/student/Loan/StudentLoan';
import StudentDashboard from '../pages/student/Dashboard/StudentDashboard';
import TeacherDashboard from '../pages/teacher/Dashboard/TeacherDashboard';

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path={paths.HOME} element={<Home />} />
        <Route path={paths.LOGIN} element={<Login />} />
        <Route path={paths.REGISTER} element={<Register />} />
        <Route path={paths.DESIGN_SYSTEM} element={<DesignSystem />} />
        <Route path={paths.COMMON_COMPONENTS} element={<CommonComponents />} />
        <Route path={paths.STUDENT_SHOP} element={<StudentShop />} />
        {/* <Route path={paths.TEACHER_SHOP} element={<TeacherShop />} /> */}
        <Route path={paths.TEACHER_MISSION} element={<TeacherMission />} />
        <Route path={paths.STUDENT_MISSION} element={<StudentMission />} />
        <Route path={paths.STUDENT_STOCK} element={<StudentStock />} />
        <Route path={paths.TEACHER_STOCK} element={<TeacherStock />} />
        <Route path={paths.TEACHER_CLASSROOM} element={<TeacherClassroom />} />
        <Route path={paths.STUDENT_CLASSROOM} element={<StudentClassroom />} />
        <Route path={paths.STUDENT_LOAN} element={<StudentLoan />} />
        <Route path={paths.STUDENT_DASHBOARD} element={<StudentDashboard />} />
        <Route path={paths.TEACHER_DASHBOARD} element={<TeacherDashboard />} />
        {/* 인증이 필요한 라우트들은 PrivateRoute로 감싸기 */}
        <Route element={<PrivateRoute />}>{/* 여기에 인증이 필요한 라우트들 추가 */}</Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
