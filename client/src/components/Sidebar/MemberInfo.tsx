import { hideGlobalAlert, showGlobalAlert } from '../../libs/store/alertStore';
import useAuthStore from '../../libs/store/authStore';
import Lufin from '../Lufin/Lufin';
import Profile from '../Profile/Profile';

const MemberInfo = () => {
  const { userName, userProfileImage, userRole, totalAsset, logout } = useAuthStore();

  const handleLogout = () => {
    showGlobalAlert(
      '로그아웃 하시겠습니까?',
      null,
      '',
      'warning',
      {
        label: '확인',
        onClick: () => {
          showGlobalAlert(
            '로그아웃 했어요.',
            null,
            '확인을 누르면 로그인 페이지로 이동합니다.',
            'info',
            {
              label: '확인',
              onClick: () => {
                logout(); // 실제 상태 초기화는 여기서
                hideGlobalAlert();
                window.location.href = '/login'; // 진짜 리다이렉션도 여기서
              },
            },
          );
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
      <div onClick={handleLogout}>
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
