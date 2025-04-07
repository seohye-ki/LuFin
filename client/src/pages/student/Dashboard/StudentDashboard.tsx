import { useEffect } from 'react';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import useAlertStore from '../../../libs/store/alertStore';
import Lufin from '../../../components/Lufin/Lufin';
import StatCard from './components/StatCard';
import CreditScoreCard from './components/CreditScoreCard';
import ClassAssetRanking from './components/ClassAssetRanking';
import ItemList from './components/ItemList';
import MissionSection from './components/MissionSection';
import AssetCard from './components/AssetCard';

const ITEMS_DATA = [
  { name: '급식줄 제일 앞에 서기', count: 1, daysLeft: 3 },
  { name: '간식 찬스', count: 2, daysLeft: 3 },
  { name: '간식 찬스', count: 2, daysLeft: 3 },
  { name: '간식 찬스', count: 2, daysLeft: 3 },
  { name: '간식 찬스', count: 2, daysLeft: 3 },
  { name: '간식 찬스', count: 2, daysLeft: 3 },
  { name: '간식 찬스', count: 2, daysLeft: 3 },
  { name: '급식줄 제일 앞에 서기', count: 3, daysLeft: 3 },
];

const StudentDashboard = () => {
  const showAlert = useAlertStore((state) => state.showAlert);

  useEffect(() => {
    // TODO: API 연동 후 실제 신용등급 확인
    const creditGrade = 'F-'; // 테스트용 하드코딩
    
    if (creditGrade === 'F-') {
      showAlert(
        '신용등급이 낮아',
        <div className="flex flex-col items-center gap-2">
          <p className="text-p1">서비스를 이용할 수 없어요.</p>
          <Lufin size="m" count={2000} />
        </div>,
        '담임 선생님과 상담 후 회생 신청을 해주세요.',
        'danger',
        {
          label: '확인',
          onClick: () => {
            // TODO: 회생 신청 모달 표시
          },
        }
      );
    }
  }, [showAlert]);

  return (
    <SidebarLayout userRole="student">
      <div className='w-full h-full flex flex-col gap-4 overflow-y-auto'>
        {/* User Profile Section */}
        <section className='flex gap-4 min-h-fit'>
          <ClassAssetRanking />
        </section>

        {/* Credit Score and Assets Section */}
        <section className='flex gap-4 min-h-[280px]'>
          <div className='flex-1'>
            <CreditScoreCard userName='최민주' creditScore={89} creditRating='A+' />
          </div>
          <div className='flex-1'>
            <AssetCard />
          </div>
        </section>

        {/* Financial Summary Section */}
        <section className='flex gap-4 min-h-fit'>
          <StatCard title='이번주 소비' amount={13000} trend={{ value: 15, isPositive: true }} />

          <StatCard title='투자' amount={33000} trend={{ value: 15, isPositive: true }} />

          <StatCard title='대출' amount={23000} />
        </section>

        {/* Items and Missions Section */}
        <section className='flex gap-4 max-h-[270px]'>
          <ItemList items={ITEMS_DATA} />
          <MissionSection
            completedCount={5}
            currentMission={{
              name: '쓰레기통 비우기',
              reward: 1000,
              daysLeft: 3,
            }}
            totalReward={123000}
          />
        </section>
      </div>
    </SidebarLayout>
  );
};

export default StudentDashboard;
