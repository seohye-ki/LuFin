import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import useAuthStore from '../libs/store/authStore';
import axiosInstance from '../libs/services/axios';

const RouteListener = () => {
  const location = useLocation();
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  useEffect(() => {
    const fetchUserData = async () => {
      if (!isAuthenticated) return;
      try {
        const res = await axiosInstance.get('/auth/my');
        const balance = res.data.data.balance;

        useAuthStore.setState((prev) => ({
          ...prev,
          totalAsset: balance, // 혹은 balance를 더하는 방식이라면 로직 맞게 수정
        }));
      } catch (err) {
        console.error('자산 정보 불러오기 실패:', err);
      }
    };

    fetchUserData();
  }, [location.pathname, isAuthenticated]);

  return null;
};

export default RouteListener;
