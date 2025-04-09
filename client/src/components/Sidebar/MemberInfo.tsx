import { tokenUtils } from '../../libs/services/axios';
import { hideGlobalAlert, showGlobalAlert } from '../../libs/store/alertStore';
import useAuthStore from '../../libs/store/authStore';
import Lufin from '../Lufin/Lufin';
import Profile from '../Profile/Profile';

const MemberInfo = () => {
  const { userName, userProfileImage, userRole, totalAsset } = useAuthStore();

  const logout = () => {
    showGlobalAlert(
      '로그아웃 하시겠습니까?',
      null,
      '확인을 누르면 로그인 페이지로 이동합니다.',
      'warning',
      {
        label: '확인',
        onClick: () => {
          ['accessToken', 'refreshToken', 'userRole'].forEach(tokenUtils.removeToken);
          window.location.href = '/login';
        },
      },
      { label: '취소', onClick: hideGlobalAlert },
    );
  };

  return (
    <div className='mt-auto flex flex-col gap-4'>
      {userRole === 'STUDENT' && (
        <div className='flex w-full h-21 flex-col bg-yellow rounded-lg p-4 gap-1'>
          <p className='text-c1 text-dark-grey'>총 자산</p>
          <Lufin count={totalAsset} size='l' />
        </div>
      )}
      <div onClick={logout}>
        <Profile
          name={userName || '로그인을 해주세요.'}
          variant='row'
          profileImage={userProfileImage || ''}
          className='border border-purple-30 rounded-lg'
        />
      </div>
    </div>
  );
};

export default MemberInfo;
