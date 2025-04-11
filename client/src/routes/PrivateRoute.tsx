import { Navigate, useLocation } from 'react-router-dom';
import React from 'react';
import useAuthStore from '../libs/store/authStore';

interface PrivateRouteProps {
  studentComponent: React.ReactElement;
  teacherComponent: React.ReactElement;
}
const PrivateRoute: React.FC<PrivateRouteProps> = ({ studentComponent, teacherComponent }) => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const userRole = useAuthStore((state) => state.userRole);
  const isLoading = useAuthStore((state) => state.isLoading);

  const location = useLocation();

  if (isLoading) {
    return <div>로딩 중...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to='/login' state={{ from: location }} replace />;
  }

  if (userRole === 'STUDENT') {
    return studentComponent;
  }

  if (userRole === 'TEACHER') {
    return teacherComponent;
  }

  return <div>권한이 없습니다.</div>;
};

export default PrivateRoute;
