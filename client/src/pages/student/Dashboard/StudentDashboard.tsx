import SidebarLayout from '../../../components/Layout/SidebarLayout';
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

const Dashboard = () => {
  return (
    <SidebarLayout>
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

export default Dashboard;
