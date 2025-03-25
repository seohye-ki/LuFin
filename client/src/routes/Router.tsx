import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { paths } from './paths';

// pages
import Home from '../pages/Home';
import Login from '../pages/Login';
import PrivateRoute from './PrivateRoute';
import CommonComponents from '../pages/CommonComponents';

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path={paths.HOME} element={<Home />} />
        <Route path={paths.LOGIN} element={<Login />} />
        <Route path={paths.COMMON_COMPONENTS} element={<CommonComponents />} />
        {/* 인증이 필요한 라우트들은 PrivateRoute로 감싸기 */}
        <Route element={<PrivateRoute />}>{/* 여기에 인증이 필요한 라우트들 추가 */}</Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
