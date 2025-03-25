import { Navigate, Outlet } from 'react-router-dom';
import { paths } from './paths';

const PrivateRoute = () => {
  // 임시로 인증 상태를 false로 설정
  const isAuthenticated = false;

  return isAuthenticated ? <Outlet /> : <Navigate to={paths.LOGIN} />;
};

export default PrivateRoute;
