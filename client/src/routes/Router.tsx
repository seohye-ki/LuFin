import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { paths } from './paths';

// pages
import Home from '../pages/Home';
import PrivateRoute from './PrivateRoute';

const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path={paths.HOME} element={<Home />} />
        {/* 인증이 필요한 라우트들은 PrivateRoute로 감싸기 */}
        <Route element={<PrivateRoute />}>
          {/* 여기에 인증이 필요한 라우트들 추가 */}
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
