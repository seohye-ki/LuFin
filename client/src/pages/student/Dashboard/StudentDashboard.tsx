import { useEffect, useState } from 'react';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import useAlertStore from '../../../libs/store/alertStore';
import Lufin from '../../../components/Lufin/Lufin';
import StatCard from './components/StatCard';
import CreditScoreCard from './components/CreditScoreCard';
import ClassAssetRanking from './components/ClassAssetRanking';
import ItemList from './components/ItemList';
import MissionSection from './components/MissionSection';
import AssetCard from './components/AssetCard';
import RecoveryApplicationModal from './components/RecoveryApplicationModal';
import axiosInstance from '../../../libs/services/axios';
import useClassroomStore from '../../../libs/store/classroomStore';

// Dashboard API response type
interface DashboardData {
  myMemberId: number;
  profileImage: string;
  rankings: {
    memberId: number;
    name: string;
    profileImage: string;
    asset: number;
    rank: number;
  }[];
  creditGrade: string;
  creditScore: number;
  creditHistories: {
    scoreChange: number;
    reason: string;
    changedAt: string;
  }[];
  totalAsset: number;
  cash: number;
  stock: number;
  loan: number;
  investmentStat: {
    label: string;
    amount: number;
    changeRate: number;
    isPositive: boolean;
  };
  consumptionStat: {
    label: string;
    amount: number;
    changeRate: number;
    isPositive: boolean;
  };
  items: {
    name: string;
    quantity: number;
    expireInDays: number;
  }[];
  ongoingMissions: {
    missionId: number;
    participationId: number;
    title: string;
    status: string;
    wage: number;
    missionDate: string;
  }[];
  totalCompletedMissions: number;
  totalWage: number;
}

const StudentDashboard = () => {
  const showAlert = useAlertStore((state) => state.showAlert);
  const hideAlert = useAlertStore((state) => state.hideAlert);
  const [dashboardData, setDashboardData] = useState<DashboardData | null>(null);
  const [loading, setLoading] = useState(true);
  const [showRecoveryModal, setShowRecoveryModal] = useState(false);
  const { currentClassId } = useClassroomStore();

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);

        const activeClassId = sessionStorage.getItem('classId');

        console.log(activeClassId);
        console.log(currentClassId);

        const response =
          currentClassId === Number(activeClassId)
            ? await axiosInstance.get('/dashboards/my')
            : await axiosInstance.get(`/dashboards/my?classId=${currentClassId}`);

        if (response.data.isSuccess) {
          setDashboardData(response.data.data);
        }
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  useEffect(() => {
    // Credit grade check for recovery application alert
    if (dashboardData && dashboardData.creditGrade === 'F-') {
      showAlert(
        '신용등급이 낮아',
        <div className='flex flex-col items-center gap-2'>
          <p className='text-p1'>서비스를 이용할 수 없어요.</p>
          <Lufin size='m' count={dashboardData.totalAsset} />
        </div>,
        '담임 선생님과 상담 후 회생 신청을 해주세요.',
        'danger',
        {
          label: '회생 신청',
          onClick: () => {
            setShowRecoveryModal(true);
            hideAlert(); // 모달을 열면서 알림 닫기
          },
        },
      );
    }
  }, [dashboardData, showAlert, hideAlert]);

  if (loading) {
    return (
      <SidebarLayout>
        <div className='flex items-center justify-center h-full'>
          <p>데이터를 불러오는 중...</p>
        </div>
      </SidebarLayout>
    );
  }

  if (!dashboardData) {
    return (
      <SidebarLayout>
        <div className='flex items-center justify-center h-full'>
          <p>데이터를 불러올 수 없습니다.</p>
        </div>
      </SidebarLayout>
    );
  }

  // Find current user name (for now using rank 1 as current user)
  const currentUserName = dashboardData.rankings.find((r) => r.rank === 1)?.name || '';

  return (
    <SidebarLayout>
      <div className='w-full h-full flex gap-4 overflow-hidden'>
        {/* Main Content */}
        <div className='flex-1 flex flex-col gap-4 overflow-y-auto pr-2'>
          {/* Credit Score and Assets Section */}
          <section className='flex gap-4 min-h-[280px]'>
            <div className='flex-1'>
              <CreditScoreCard
                userName={currentUserName}
                creditScore={dashboardData.creditScore}
                creditRating={dashboardData.creditGrade}
                recentActivities={dashboardData.creditHistories.map((history) => ({
                  type: history.scoreChange > 0 ? 'increase' : 'decrease',
                  amount: Math.abs(history.scoreChange),
                  description: history.reason,
                  date: history.changedAt,
                }))}
              />
            </div>
            <div className='flex-1'>
              <AssetCard
                assets={{
                  cash: dashboardData.cash,
                  stock: dashboardData.stock,
                  loan: dashboardData.loan,
                }}
                totalAsset={dashboardData.totalAsset}
              />
            </div>
          </section>

          {/* Financial Summary Section */}
          <section className='flex gap-4 min-h-fit'>
            <StatCard
              title={dashboardData.consumptionStat.label}
              amount={dashboardData.consumptionStat.amount}
              trend={{
                value: dashboardData.consumptionStat.changeRate,
                isPositive: dashboardData.consumptionStat.isPositive,
              }}
            />

            <StatCard
              title={dashboardData.investmentStat.label}
              amount={dashboardData.investmentStat.amount}
              trend={{
                value: dashboardData.investmentStat.changeRate,
                isPositive: dashboardData.investmentStat.isPositive,
              }}
            />

            <StatCard title='대출' amount={dashboardData.loan} />
          </section>

          {/* Items and Missions Section */}
          <section className='flex gap-4 max-h-[270px]'>
            <ItemList
              items={dashboardData.items.map((item) => ({
                name: item.name,
                count: item.quantity,
                daysLeft: item.expireInDays,
              }))}
            />
            <MissionSection
              completedCount={dashboardData.totalCompletedMissions}
              currentMission={
                dashboardData.ongoingMissions[0]
                  ? {
                      name: dashboardData.ongoingMissions[0].title,
                      reward: dashboardData.ongoingMissions[0].wage,
                      daysLeft: Math.ceil(
                        (new Date(dashboardData.ongoingMissions[0].missionDate).getTime() -
                          new Date().getTime()) /
                          (1000 * 60 * 60 * 24),
                      ),
                    }
                  : undefined
              }
              totalReward={dashboardData.totalWage}
            />
          </section>
        </div>

        {/* Right Sidebar - Rankings */}
        <div className='w-64 h-full'>
          <ClassAssetRanking
            rankings={dashboardData.rankings}
            myMemberId={dashboardData.myMemberId}
          />
        </div>
      </div>

      {showRecoveryModal && (
        <RecoveryApplicationModal
          studentName={currentUserName}
          creditGrade={dashboardData.creditGrade}
          currentAsset={dashboardData.totalAsset}
          onClose={() => setShowRecoveryModal(false)}
          onSubmit={() => {
            setShowRecoveryModal(false);
          }}
        />
      )}
    </SidebarLayout>
  );
};

export default StudentDashboard;
